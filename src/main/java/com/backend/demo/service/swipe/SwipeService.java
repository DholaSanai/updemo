package com.backend.demo.service.swipe;

import com.backend.demo.dto.property.PropertyPartnerNameResponseBody;
import com.backend.demo.dto.swipe.GetAllSwipes;
import com.backend.demo.dto.user.chillowUser.ChillowImageDto;
import com.backend.demo.dto.user.chillowUser.ChillowUserDto;
import com.backend.demo.entity.property.PropertyPartner;
import com.backend.demo.entity.property.PropertyPartnerCategory;
import com.backend.demo.entity.property.UserPreferredPropertyPartner;
import com.backend.demo.entity.swipe.Swipe;
import com.backend.demo.feignclient.user.HubSpot;
import com.backend.demo.firebase.swipe.firebaseModels.FirebaseSwipeMatchModel;
import com.backend.demo.firebase.swipe.firebaseServices.FirebaseSwipe;
import com.backend.demo.model.ChillowUserByDistance;
import com.backend.demo.repository.property.*;
import com.backend.demo.repository.swipe.SwipeRepository;
import com.backend.demo.repository.user.ChillowUserVerifyRepository;
import com.backend.demo.repository.user.UserRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SwipeService {
    private static final Logger logger = Logger.getLogger(SwipeService.class.getName());
    //    @Autowired
//    UserAndNotificationServiceClient userAndNotificationServiceClient;
    @Autowired
    private SwipeRepository swipeRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FirebaseSwipe firebaseSwipeService;
    @Autowired
    private ListedPropertyRepository listedPropertyRepository;
    @Autowired
    private HubSpot hubSpot;
    @Autowired
    private ChillowUserVerifyRepository chillowUserVerifyRepository;

    @Autowired
    private PropertyPartnerCategoryRepository propertyPartnerCategoryRepository;
    @Autowired
    private PropertyPartnerRepository propertyPartnerRepository;

    @Autowired
    private UserPrefferedPropertyPartnerRepository userPrefferedPropertyPartnerRepository;
    @Autowired
    private UserPropertyJunctionTableRepository userPropertyJunctionTableRepository;
    @Autowired
    private PropertyPartnerCategoryJunctionTableRepository propertyPartnerCategoryJunctionTableRepository;
    @Autowired
    private PropertyPartnerLocationRepository propertyPartnerLocationRepository;
    @Autowired
    private PropertyPartnerAmenitiesRepository propertyPartnerAmenitiesRepository;

    public Optional<Swipe> checkIfUserRightSwiped(String userId, String shownUserId) {
        return swipeRepository.findByUserIdAndShownUserIdAndIsSwipedLeftFalseAndIsDeletedFalse(userId, shownUserId);
    }


//    public List<ChillowUserDto> getUserByIds(List<String> ids) {
//        return userAndNotificationServiceClient.getUsersByIds(ids);
//    }

    public void saveNewSwipe(String userId, String showUserId, boolean isLiked, boolean isSwipedRight,
                             boolean isSwipedLeft) {
        if (swipeRepository.existsByUserIdAndShownUserIdAndIsDeletedFalse(userId, showUserId)) {
            logger.info("Swipe already exists");
            return;
        }
        Swipe newSwipe = new Swipe(UUID.randomUUID().toString(), userId, showUserId, isLiked, isSwipedRight,
                isSwipedLeft, false);
        swipeRepository.save(newSwipe);
        logger.info("Swipe saved");
    }

    public void saveSwipeMatchToFirebase(boolean isMatched, String matchedId, String isRoomOwner, ChillowUserDto user, ChillowUserDto matchedUser) {
        FirebaseSwipeMatchModel userModel;
        FirebaseSwipeMatchModel matchedUserModel;
        if (!isMatched) {
            userModel = new FirebaseSwipeMatchModel(false, true, matchedId, user.getId(), matchedUser.getId(),
                    matchedUser.getName(), null, null, null, null, null, false);
            matchedUserModel = new FirebaseSwipeMatchModel(false, true, matchedId, matchedUser.getId(), user.getId(),
                    user.getName(), null, null, null, null, null, false);
        } else {
            ChillowImageDto matchedUserImage = !CollectionUtils.isEmpty(matchedUser.getChillowUserImages()) ?
                    matchedUser.getChillowUserImages().get(0) : null;

            ChillowImageDto userImage = !CollectionUtils.isEmpty(user.getChillowUserImages()) ?
                    user.getChillowUserImages().get(0) : null;

            userModel = new FirebaseSwipeMatchModel(true, true, matchedId, user.getId(), matchedUser.getId(),
                    matchedUser.getName(), user.getId(), matchedUser.getId(), isRoomOwner, userImage, matchedUserImage, false);


            matchedUserModel = new FirebaseSwipeMatchModel(false, true, matchedId, matchedUser.getId(), user.getId(),
                    user.getName(), user.getId(), matchedUser.getId(), isRoomOwner, matchedUserImage, userImage, false);
        }
        try {
            firebaseSwipeService.save(userModel, matchedUserModel);
            logger.info("Saving to firebase");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<ChillowUserDto> getAllFromIds(Set<String> userIds) throws FeignException.FeignClientException {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.EMPTY_LIST;
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        List<ChillowUserDto> matchedUsers = userRepository.findByIdInOrderByLastLoginDesc(new ArrayList<>(userIds)).stream().
                map(x -> modelMapper.map(x, ChillowUserDto.class)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(matchedUsers)) {
            return matchedUsers;
        }
        return Collections.EMPTY_LIST;
    }

    public List<ChillowUserByDistance> getAllWithLocation(String userId, Double longitude, Double latitude) {
        if (userId.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return swipeRepository.findUsersWithLocation(userId, longitude, latitude, 100);
    }

    public List<GetAllSwipes> getAllSwipes(String userId, Double longitude, Double latitude, Integer minAge,
                                           Integer maxAge, List<String> pronouns) {
        List<Swipe> swipesOfUser = swipeRepository.findByUserIdAndIsDeletedAndIsSwipedLeftFalse(userId, false);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<ChillowUserDto> userDetails = new ArrayList<>();
        List<ChillowUserByDistance> users = getAllWithLocation(userId, longitude, latitude);
        if (!CollectionUtils.isEmpty(users)) {
            userDetails.addAll(getAllFromIds(users.stream().map(ChillowUserByDistance::getId).
                    collect(Collectors.toSet())));
        }
        if (!CollectionUtils.isEmpty(swipesOfUser)) {
            Set<String> userIds = swipesOfUser.stream().map(Swipe::getShownUserId).collect(Collectors.toSet());
            userDetails.addAll(getAllFromIds(userIds));
        }
        try {
            List<GetAllSwipes> allSwipes = userDetails.stream().map(u -> {
                        Optional<Swipe> currentSwipe = swipesOfUser.stream().
                                filter(x -> x.getShownUserId().equals(u.getId())).findFirst();
                        GetAllSwipes swipe = modelMapper.map(u, GetAllSwipes.class);

                        LocalDate minimumDate = LocalDate.now().minusDays(30);
                        if (swipe.getLastLogin().isBefore(minimumDate)) {
                            swipe.setUserExpired(true);
                        }

                        if (currentSwipe.isPresent()) {
                            swipe.setIsSwipedLeft(currentSwipe.get().getIsSwipedLeft());
                            swipe.setIsSwipedRight(currentSwipe.get().getIsSwipedRight());
                        } else {
                            swipe.setIsSwipedLeft(false);
                            swipe.setIsSwipedRight(false);
                        }
                        return swipe;

                    }).filter(x -> !x.getIsSwipedRight()) // !x.getIsSwipedLeft() && !x.getIsSwipedRight()
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(allSwipes)) {
                return Collections.EMPTY_LIST;
            }
            allSwipes = filterAgainstLastLogin(allSwipes);

            if (pronouns != null && !pronouns.isEmpty()) {
                allSwipes = filterAgainstPronouns(pronouns, allSwipes);
            }
//            if(minAge != 0 && maxAge != 200) {
            allSwipes = filterAgainstAgeRange(minAge, maxAge, allSwipes);
//            }
            //CALL A FUNCTION TO SET PROPERTIES HERE
            return setPropertyPartnerValues(allSwipes);

        } catch (Exception exception) {
            log.error("Error occured while getting data", exception);
        }
        return Collections.EMPTY_LIST;
    }

    private List<GetAllSwipes> filterAgainstLastLogin(List<GetAllSwipes> allSwipes) {
        List<GetAllSwipes> newSwipeList = new ArrayList<>();
        for (GetAllSwipes eachSwipe : allSwipes) {
//            int age = Period.between(eachSwipe.getBirthDate(), LocalDate.now()).getYears();
            LocalDate currentDate = LocalDate.now();
            LocalDate userLastLoginDate = eachSwipe.getLastLogin();
            if (!currentDate.minusDays(60).isAfter(userLastLoginDate)) {
                newSwipeList.add(eachSwipe);
            }
        }
        return newSwipeList;
    }

    private List<GetAllSwipes> setPropertyPartnerValues(List<GetAllSwipes> getAllSwipes) {

        for (GetAllSwipes eachUser : getAllSwipes) {

            List<UserPreferredPropertyPartner> userPreferredPropertyPartnerList = userPrefferedPropertyPartnerRepository.findByUserIdContainingAndIsPreferenceDeletedFalse(eachUser.getId());

            List<PropertyPartnerNameResponseBody> generalLivingList = new ArrayList<>();
            List<PropertyPartnerNameResponseBody> coLivingList = new ArrayList<>();
            List<PropertyPartnerNameResponseBody> offCampusList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(userPreferredPropertyPartnerList)) {

                for (UserPreferredPropertyPartner eachProperty : userPreferredPropertyPartnerList) {

                    Optional<PropertyPartnerCategory> propertyPartnerCategory = propertyPartnerCategoryRepository.findById(eachProperty.getPropertyPartnerCategoryId());
                    if (propertyPartnerCategory.isPresent()) {
                        PropertyPartnerCategory presentPropertyPartnerCategory = propertyPartnerCategory.get();

                        Optional<PropertyPartner> propertyPartner = propertyPartnerRepository.findById(eachProperty.getPropertyPartner());
                        if (propertyPartner.isPresent()) {
                            PropertyPartner presentProperty = propertyPartner.get();

                            PropertyPartnerNameResponseBody propertyPartnerNameResponseBody =
                                    new PropertyPartnerNameResponseBody(presentProperty.getId(), presentProperty.getComplexName());
                            if (presentPropertyPartnerCategory.getTypeName().equals("General Living")) {
                                generalLivingList.add(propertyPartnerNameResponseBody);
                            }
                            if (presentPropertyPartnerCategory.getTypeName().equals("Off-campus Housing")) {
                                offCampusList.add(propertyPartnerNameResponseBody);
                            }
                            if (presentPropertyPartnerCategory.getTypeName().equals("Co-living")) {
                                coLivingList.add(propertyPartnerNameResponseBody);
                            }
                        }
                    }
                }
                eachUser.setGeneralLivingList(generalLivingList);
                eachUser.setCoLivingList(coLivingList);
                eachUser.setOffCampusList(offCampusList);
            } else {
                eachUser.setGeneralLivingList(Collections.emptyList());
                eachUser.setCoLivingList(Collections.emptyList());
                eachUser.setOffCampusList(Collections.emptyList());
            }
        }
        Collections.shuffle(getAllSwipes);
        return getAllSwipes;
    }

    private List<GetAllSwipes> filterAgainstAgeRange(Integer minAge, Integer maxAge, List<GetAllSwipes> allSwipes) {
        List<GetAllSwipes> newSwipeList = new ArrayList<>();
        for (GetAllSwipes eachSwipe : allSwipes) {
            int age = Period.between(eachSwipe.getBirthDate(), LocalDate.now()).getYears();
            if (age >= minAge && age <= maxAge) {
                newSwipeList.add(eachSwipe);
            }
        }
        return newSwipeList;
    }

    private List<GetAllSwipes> filterAgainstPronouns(List<String> pronouns, List<GetAllSwipes> allSwipes) {
        List<GetAllSwipes> newSwipeList = new ArrayList<>();
        for (GetAllSwipes eachSwipe : allSwipes) {
            for(String eachPronoun : pronouns) {
                if (eachSwipe.getPronouns().equals(eachPronoun)) {
                    newSwipeList.add(eachSwipe);
                }
            }
        }
        return newSwipeList;
    }


    public boolean resetSwipeOfUser(String id) {
        if (StringUtils.isEmpty(id)) {
            return false;
        }
        List<Swipe> userSwipes = swipeRepository.findByUserId(id);
        userSwipes.forEach(swipe -> {
            swipe.setIsSwipedLeft(false);
        });
        swipeRepository.saveAll(userSwipes);
        return true;
    }

    public boolean deleteSwipesOfUser(String id) {
        if (StringUtils.isEmpty(id)) {
            return false;
        }
        List<Swipe> userSwipes = swipeRepository.findByUserId(id);
        userSwipes.forEach(swipe -> {
            swipe.setIsDeleted(true);
        });
        swipeRepository.saveAll(userSwipes);
        return true;
    }

    public boolean clearAllLeftSwipesOfUser(String userId) {
        //hard delete swiped lleft yy
        List<Swipe> allUserLeftSwipes = swipeRepository.findAllByUserIdAndIsSwipedLeftTrue(userId);
        if (!CollectionUtils.isEmpty(allUserLeftSwipes)) {
            swipeRepository.deleteAll(allUserLeftSwipes);
            return true;
        }
        return true;
    }
}
