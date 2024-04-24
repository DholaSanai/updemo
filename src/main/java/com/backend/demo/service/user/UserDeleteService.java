package com.backend.demo.service.user;

import com.backend.demo.entity.property.ListedProperty;
import com.backend.demo.entity.user.ChillowUser;
import com.backend.demo.repository.property.ListedPropertyRepository;
import com.backend.demo.repository.user.UserRepository;
import com.backend.demo.service.room.ChillowRoomService;
import com.backend.demo.service.match.MatchService;
import com.backend.demo.service.swipe.SwipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserDeleteService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChillowRoomService roomService;

    @Autowired
    private ListedPropertyRepository listedPropertyRepository;

    @Autowired
    private SwipeService swipeService;

    @Autowired
    private MatchService matchService;


    public Boolean deleteUserProfile(String userId) {
        Optional<ChillowUser> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            existingUser.get().setNotificationsEnabled(false);
            existingUser.get().setNumber(null);
            existingUser.get().setDeleted(true);
            existingUser.get().setUserExpired(true);
            existingUser.get().getNotificationSessions().forEach(eachNotification -> eachNotification.setDeviceId(StringUtils.EMPTY));
            deleteUserProperties(userId);
//            existingUser.get().setNewUser(true);
//            existingUser.get().setLastLogin(null);
//            existingUser.get().setInitialLogin(false);
//            existingUser.get().setUserExpired(true);
//            existingUser.get().setEmail(StringUtils.EMPTY);
//            existingUser.get().setPronouns(StringUtils.EMPTY);
//            existingUser.get().setAboutMe(StringUtils.EMPTY);
//            existingUser.get().setName(StringUtils.EMPTY);
//            existingUser.get().setBirthDate(null);
//            existingUser.get().setNumber(StringUtils.EMPTY);
//            existingUser.get().setAuthToken(StringUtils.EMPTY);
//            existingUser.get().setFacebookToken(StringUtils.EMPTY);
//            existingUser.get().setGoogleToken(StringUtils.EMPTY);
//            existingUser.get().setAppleToken(StringUtils.EMPTY);
//            existingUser.get().getChillowHousing().
//            existingUser.get().setLoginType(StringUtils.EMPTY);
//            existingUser.get().getLocation().setCity(StringUtils.EMPTY);
//            existingUser.get().getLocation().setLatitude(1.1);
//            existingUser.get().getLocation().setLongitude(1.1);
//            existingUser.get().getLocation().setCountry(StringUtils.EMPTY);
//            existingUser.get().getLocation().setNeighborhood(StringUtils.EMPTY);
//            existingUser.get().getLocation().setAddress(StringUtils.EMPTY);
//            existingUser.get().getLocation().setState(StringUtils.EMPTY);
//            existingUser.get().getChillowUserPreferences().reset();
//            existingUser.get().getChillowUserVerify().reset();
//            existingUser.get().getChillowUserInterests().reset();
//            existingUser.get().getChillowUserImages().forEach(eachImage -> eachImage.setIsDeleted(true));
//            existingUser.get().setDeleted(true);
//            existingUser.get().setInitialLogin(true);
//            userRepository.save(existingUser.get());
//            swipeService.deleteSwipesOfUser(userId);
//            matchService.deleteUserMatches(userId);
//            roomService.deleteRoomsOfUser(userId);
            userRepository.save(existingUser.get());
            log.info("User deleted successfully");
            return true;
        }
        return false;
    }

    private void deleteUserProperties(String userId) {
        List<ListedProperty> listedPropertyList = listedPropertyRepository.findByOwnerId(userId);
        if(!CollectionUtils.isEmpty(listedPropertyList)){
            for(ListedProperty eachProperty : listedPropertyList){
                eachProperty.setIsListingDeleted(true);
            }
            listedPropertyRepository.saveAll(listedPropertyList);
        }
    }

}
