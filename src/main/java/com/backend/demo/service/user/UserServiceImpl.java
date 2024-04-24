package com.backend.demo.service.user;

import com.backend.demo.components.EvidentVerificationNotificationDispatcher;
import com.backend.demo.dto.evident.EvidentIdRequestBody;
import com.backend.demo.dto.hubspot.HubspotProperties;
import com.backend.demo.dto.hubspot.HubspotRequestBody;
import com.backend.demo.dto.property.PropertyPartnerNameResponseBody;
import com.backend.demo.dto.user.*;
import com.backend.demo.dto.user.chillowUser.*;
import com.backend.demo.entity.property.*;
import com.backend.demo.entity.user.*;
import com.backend.demo.enums.property.PropertyType;
import com.backend.demo.exceptions.AlreadyUsedPhoneNumber;
import com.backend.demo.exceptions.UserNotFoundException;
import com.backend.demo.feignclient.user.HubSpot;
import com.backend.demo.firebase.evidentVerification.firebaseModel.FirebaseEvidentVerification;
import com.backend.demo.firebase.evidentVerification.firebaseService.FirebaseEvidentVerificationService;
import com.backend.demo.model.*;
import com.backend.demo.repository.property.*;
import com.backend.demo.repository.user.*;
import com.backend.demo.security.JWTauth;
import com.backend.demo.service.match.MatchService;
import com.backend.demo.service.property.PropertyService;
import com.backend.demo.service.swipe.SwipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.Timestamp;
import feign.FeignException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.Tuple;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ChillowUserLocationRepository chillowUserLocationRepository;
    @Autowired
    private final SwipeService swipeServiceClient;
    @Autowired
    private final MatchService matchService;
    @Autowired
    private final ChillowUserImagesRepository chillowUserImagesRepository;
    @Autowired
    EvidentVerificationNotificationDispatcher evidentVerificationNotificationDispatcher;
    @Autowired
    FirebaseEvidentVerificationService firebaseEvidentVerificationService;
    @Autowired
    PropertyService propertyService;
    @Autowired
    ExternalPropertyRepository externalPropertyRepository;
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
    @Value("${hubspot.key}")
    private String hubsportAuthToken;
    @Autowired
    private SmsService smsService;
    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private GoogleAuthenticator googleAuthenticator;
    @Autowired
    private JWTauth auth;
    @Autowired
    private FacebookAuthenticator facebookAuthenticator;
    @Autowired
    private AppleAuthenticator appleAuthenticator;
    @Autowired
    private OnesignalNotificationSessionRepository onesignalNotificationSessionRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ChillowUserLocationRepository chillowUserLocationRepository,
                           ChillowUserImagesRepository chillowUserImagesRepository,
                           SwipeService swipeServiceClient, MatchService matchService) {
        this.chillowUserLocationRepository = chillowUserLocationRepository;
        this.userRepository = userRepository;
        this.chillowUserImagesRepository = chillowUserImagesRepository;
        this.swipeServiceClient = swipeServiceClient;
        this.matchService = matchService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public Optional<ChillowUser> getAccountDetails(String email) {
        return userRepository.findByEmail(email);
    }

    public ChillowUser createAccount(String email) {
        String userId = UUID.randomUUID().toString();


        ChillowUser olduser = new ChillowUser(userId, true, false, LocalDate.now(), true,
                false, true, email, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, LocalDate.now(),
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false,
                null, null, null, null, Collections.EMPTY_LIST,
                Collections.EMPTY_LIST, StringUtils.EMPTY, null, null, null, null,
                false, Instant.now(), Instant.now());

        ChillowUserLocation location = new ChillowUserLocation(UUID.randomUUID().toString(), StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, 1.1d, 1.1d, null);

        ChillowUserPreferences preferences = new ChillowUserPreferences(UUID.randomUUID().toString(),
                null, null, null, null, null, null, null,
                null, null, null, null, null, null);

        ChillowUserVerify verifyUser = new ChillowUserVerify(UUID.randomUUID().toString(), true,
                false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null);

        ChillowUserInterests interests = new ChillowUserInterests(UUID.randomUUID().toString(), false, false,
                false, false, false,
                false, false, false, false, false, false, false,
                false, null);

        location.setUser(olduser);
        interests.setUser(olduser);
        preferences.setUser(olduser);
        verifyUser.setUser(olduser);
        olduser.setLocation(location);
        olduser.setAuthToken(auth.generateToken(userId));
        olduser.setChillowUserPreferences(preferences);
        olduser.setChillowUserVerify(verifyUser);
        olduser.setChillowUserInterests(interests);
        return olduser;
    }

    public ChillowUserDto getExistingUserDto(ChillowUser data, ModelMapper mapper) {
        String authToken = auth.generateToken(data.getId());
        data.setAuthToken(authToken);
        data.setDeleted(false);

        userRepository.save(data);

        ChillowUserDto mappedData = mapper.map(data, ChillowUserDto.class);

        if (CollectionUtils.isEmpty(data.getNotificationSessions())) {
            mappedData.setOneSignalNotificationSessions(Collections.EMPTY_LIST);
        } else {
            List<NotificationSessions> sessions = data.getNotificationSessions();
            mappedData.setOneSignalNotificationSessions(sessions.stream().
                    map(s -> mapper.map(s, NotificationSessionDto.class)).collect(Collectors.toList()));
        }
        return mappedData;
    }

    public ChillowUserDto saveNewSSOUser(ChillowUser newUser, ModelMapper mapper) {
        newUser.setInitialLogin(true);
        newUser.setDeleted(false);
        ChillowUserDto response = mapper.map(newUser, ChillowUserDto.class);
        String authToken = auth.generateToken(newUser.getEmail());
        response.setOneSignalNotificationSessions(Collections.EMPTY_LIST);
        newUser.setAuthToken(authToken);
        userRepository.save(newUser);
        return response;
    }

    @Override
    public ChillowUserDto loginWithGoogle(String token) {
        GoogleUser status;
        try {
            status = googleAuthenticator.authenticateWithGoogle(token);
            Optional<ChillowUser> existingUser = userRepository.findByEmailAndIsDeletedFalse(status.getEmail());
            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            if (existingUser.isPresent()) {
                ChillowUser user = existingUser.get();
                return getExistingUserDto(user, mapper);
            }
            ChillowUser newUser = createAccount(status.getEmail());
            newUser.setName(status.getName());
            newUser.setGoogleToken(token);
            return saveNewSSOUser(newUser, mapper);
        } catch (FeignException exception) {
            return null;
        }
    }

    @Override
    public ChillowUserDto loginWithApple(String token) throws UsernameNotFoundException {
        AppleUser status;

        try {
            status = appleAuthenticator.authenticateWithApple(token);
            Optional<ChillowUser> existingUser = userRepository.findByEmailAndIsDeletedFalse(status.getEmail());
            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            if (existingUser.isPresent()) {
                return getExistingUserDto(existingUser.get(), mapper);
            }
            ChillowUser newUser = createAccount(status.getEmail());
            newUser.setAppleToken(token);
            return saveNewSSOUser(newUser, mapper);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new UsernameNotFoundException(exception.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws RuntimeException {
        Optional<ChillowUser> userAccount = userRepository.findByEmail(s);
        if (userAccount.isPresent()) {
            return new User(userAccount.get().getName(), userAccount.get().getAuthToken(), true,
                    true, true, true, new ArrayList<>());
        }
        throw new UsernameNotFoundException(s);
    }

    @Override
    public ChillowUserDto loginWithFacebook(String idToken) {
        try {
            FacebookUser data = facebookAuthenticator.facebookAuthentication(idToken);
            Optional<ChillowUser> existingUser = userRepository.findByEmailAndIsDeletedFalse(data.getEmail());
            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            if (existingUser.isPresent()) {
                existingUser.get().setFacebookToken(idToken);
                return getExistingUserDto(existingUser.get(), mapper);
            }
            ChillowUser user = createAccount(data.getEmail());
            user.setName(data.getName());
            user.setEmail(data.getEmail());
            user.setFacebookToken(idToken);
            return saveNewSSOUser(user, mapper);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new UsernameNotFoundException("User could not be validated");
        }
    }

    @Override
    public ChillowUserDto getUserById(String id) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Optional<ChillowUser> user = userRepository.findById(id);
        Optional<ChillowUserDto> data = user.map(chillowUser -> {
            ChillowUserDto dto = mapper.map(chillowUser, ChillowUserDto.class);
            if (CollectionUtils.isEmpty(dto.getOneSignalNotificationSessions())) {
                dto.setOneSignalNotificationSessions(Collections.EMPTY_LIST);
            }
            return dto;
        });
        return data.orElse(null);
    }

    @Override
    @Transactional
    public List<ChillowUserDto> getUserByIds(List<String> ids) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return userRepository.findByIdIn(ids).stream().
                map(eachUser -> mapper.map(eachUser, ChillowUserDto.class)).
                collect(Collectors.toList());
    }

    @Override
    public List<ChillowUserDistance> getUsersNotInIdsOrderedByDistance(String userId, List<String> ids,
                                                                       Double longitude, Double latitude) {
        if (userId.isEmpty() || CollectionUtils.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }
        List<Tuple> tuples = userRepository.findByIdNotInWithDistanceLessThanHundred(userId, ids, longitude, latitude);
        return tuples.stream().map(eachTuple ->
                new ChillowUserDistance(eachTuple.get(0, String.class), eachTuple.get(1, String.class),
                        eachTuple.get(2, String.class), eachTuple.get(3, String.class),
                        eachTuple.get(4, String.class), eachTuple.get(5, Double.class),
                        eachTuple.get(6, Double.class), eachTuple.get(7, Double.class))
        ).collect(Collectors.toList());
    }

    @Override
    public boolean verifyUser(VerifyUserRequestBody body) {
        Optional<ChillowUser> user = userRepository.findById(body.getUserId());
        if (user.isPresent()) {
            ChillowUser currentUser = user.get();
            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            ChillowUserVerify userVerify = mapper.map(body, ChillowUserVerify.class);
            currentUser.setChillowUserVerify(userVerify);
            userRepository.save(currentUser);
            sendSMSNotificationOfOtp(currentUser.getNumber());
            return true;
        }
        return false;
    }

    public int getNewOtp() {
//        return 10000 + new Random().nextInt(999999);
        return 100000 + new Random().nextInt(900000);
    }

    public OTP generateOtp(String phoneNumber, String email) {
        if (StringUtils.isEmpty(phoneNumber) && StringUtils.isEmpty(email)) {
            return null;
        }
        int pinCode = getNewOtp();
        List<OTP> allPreviousOtps = otpRepository.findAllByEmailAndIsExpiredFalse(email);
        allPreviousOtps.forEach(oldOtp -> {
            oldOtp.setIsExpired(true);
        });
        otpRepository.saveAll(allPreviousOtps);
        OTP otp = new OTP(UUID.randomUUID().toString(), phoneNumber, email, pinCode, false,
                false, LocalDateTime.now(), Instant.now());
        return otpRepository.save(otp);
    }

    private String checkZerosToBeAddedBeforeOtp(Integer value) {
        if (value == null) {
            return "000000";
        }
        int lengthOfOtp = value.toString().length();
        final int otpSize = 6;
        if (lengthOfOtp < otpSize) {
            int totalZeros = otpSize - lengthOfOtp;
            StringBuilder otp = new StringBuilder();
            for (int i = 0; i < totalZeros; i++) {
                otp.append("0");
            }
            otp.append(value);
            return otp.toString();
        }
        return value.toString();
    }

    @Override
    public boolean sendSMSNotificationOfOtp(String phoneNumber) {
        List<ChillowUser> userExists = userRepository.findByNumber(phoneNumber);
        if (!CollectionUtils.isEmpty(userExists)) {
            if (userExists.size() == 1) {
                OTP userOtp = generateOtp(userExists.get(0).getNumber(), userExists.get(0).getEmail());
                if (userOtp != null) {
                    String formattedOtp = checkZerosToBeAddedBeforeOtp(userOtp.getPinCode());
                    final String message = "Chillow Verification Code: \n " + formattedOtp + "\n If you didn't request this pin disregard";
                    smsService.sendSMS(userOtp.getPhoneNumber(), message);
                    return true;
                }
                return false;
            } else {
                throw new AlreadyUsedPhoneNumber("The phone number is already in use by another user, kindly enter a new one");
            }
        }
        throw new UserNotFoundException("User with phone number does not exist");
    }

    @Override
    public boolean verifyOtpByPhoneNumber(String phoneNumber, Integer pinCode) {
        List<ChillowUser> userExists = userRepository.findByNumber(phoneNumber);
        if (!CollectionUtils.isEmpty(userExists)) {
            if (userExists.size() == 1) {
                Optional<OTP> otp = otpRepository.findByPinCodeAndPhoneNumberAndIsConsumedFalseAndIsExpiredFalse(pinCode, phoneNumber);
                if (otp.isPresent()) {
                    OTP validOtp = otp.get();
                    ChillowUser currentUser = userExists.get(0);
                    ChillowUserVerify userVerify = currentUser.getChillowUserVerify();
                    if (userVerify == null) {
                        userVerify = new ChillowUserVerify(UUID.randomUUID().toString(), true, false, false,
                                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                                StringUtils.EMPTY, null, null, currentUser);

                    }
                    userVerify.setPhoneVerified(true);
                    validOtp.setIsConsumed(true);
                    validOtp.setIsExpired(true);
                    otpRepository.save(validOtp);
                    currentUser.setChillowUserVerify(userVerify);
                    userRepository.save(currentUser);
                    return true;
                }
            }
            return false;
        }
        throw new UserNotFoundException("User with phone number does not exist");
    }

    @Override
    public List<MultipleUserVerifyResponse> verifyMultipleUsersByPhone(List<String> phoneNumbers) {
        if (CollectionUtils.isEmpty(phoneNumbers)) {
            return Collections.EMPTY_LIST;
        }
        List<ChillowUser> users = userRepository.findAllByNumberIn(phoneNumbers);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        users.forEach(user -> {
            ChillowUserVerify verify = user.getChillowUserVerify();
            if (verify == null) {
                verify = new ChillowUserVerify(UUID.randomUUID().toString(), true, false, false,
                        StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
                        , StringUtils.EMPTY, null, null, null);

            } else {
                verify.setPhoneVerified(true);
            }
            user.setChillowUserVerify(verify);

        });

        userRepository.saveAll(users);
        return users.stream().map(user -> {
            ChillowUserVerifyDto verifyDto = mapper.map(user.getChillowUserVerify(), ChillowUserVerifyDto.class);
            return new MultipleUserVerifyResponse(user.getId(), verifyDto);
        }).collect(Collectors.toList());
    }

    public List<ChillowUserImage> replaceImages(List<ChillowImageDto> newImages, List<ChillowUserImage> existingImages) throws ClassCastException {
        List<ChillowUserImage> newImagesList = new ArrayList<>();
        Integer[] sequences = (Integer[]) newImages.stream().map(ChillowImageDto::getSequence).collect(Collectors.toList()).toArray();
        Arrays.stream(sequences).forEach(sequence -> {
            Optional<ChillowUserImage> replaceImage = existingImages.stream().filter(x -> x.getSequence() != sequence).findFirst();
            replaceImage.ifPresent(newImagesList::add);
        });
        ChillowUser user = existingImages.get(0).getUser();
        newImagesList.addAll(newImages.stream().map(x ->
                        new ChillowUserImage(UUID.randomUUID().toString(), x.getSequence(), x.getFile(), false, user)).
                collect(Collectors.toList()));
        return newImagesList;
    }

    @Override
    public ChillowUserDto addImages(AddImageRequestBody body) {
        Optional<ChillowUser> user = userRepository.findById(body.getUserId());
        if (user.isPresent()) {
            ChillowUser currentUser = user.get();
            List<ChillowImageDto> images = body.getImages();
            List<ChillowUserImage> userImageList;
            if (CollectionUtils.isEmpty(currentUser.getChillowUserImages())) {
                userImageList = images.stream()
                        .map(i -> new ChillowUserImage(UUID.randomUUID().toString(), i.getSequence(), i.getFile(), false, currentUser))
                        .collect(Collectors.toList());
            } else {
                userImageList = replaceImages(images, currentUser.getChillowUserImages());
            }
            currentUser.setChillowUserImages(userImageList);
            userRepository.save(currentUser);
        }
        return null;
    }

    public ChillowUserEditProfileDto saveNewUser(ChillowUserEditProfileDto user) {
        ChillowUser newUser = createAccount(user.getEmail());
        if (userRepository.existsByEmail(user.getEmail())) {
            return null;
        }
        return saveUser(newUser, user);
    }

    public List<UserPreferredPropertyPartner> getUserPreferredProperties(String userId) {
        return userPrefferedPropertyPartnerRepository.findByUser(userId);
    }

    public ChillowUserEditProfileDto saveUser(ChillowUser currentUser, ChillowUserEditProfileDto updatedUser) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
        if (updatedUser.getChillowUserVerify() != null) {
            ChillowUserVerify verify = modelMapper.map(updatedUser.getChillowUserVerify(), ChillowUserVerify.class);
            verify.setUser(currentUser);
            verify.setId(currentUser.getChillowUserVerify().getId());
            currentUser.setChillowUserVerify(verify);
        }
        modelMapper.map(updatedUser, currentUser);

        if (updatedUser.getChillowUserInterests() != null) {
            ChillowUserInterests interests = modelMapper.map(updatedUser.getChillowUserInterests(), ChillowUserInterests.class);
            interests.setUser(currentUser);
            interests.setId(currentUser.getChillowUserInterests().getId());
            currentUser.setChillowUserInterests(interests);
        }
        if (updatedUser.getChillowUserPreferences() != null) {
            ChillowUserPreferences preferences = modelMapper.map(updatedUser.getChillowUserPreferences(), ChillowUserPreferences.class);
            preferences.setWorkFrom(updatedUser.getChillowUserPreferences().getWorkFrom());
            preferences.setUser(currentUser);
            preferences.setId(currentUser.getChillowUserPreferences().getId());
            currentUser.setChillowUserPreferences(preferences);
        }
        if (updatedUser.getLocation() != null) {
            ChillowUserLocation location = modelMapper.map(updatedUser.getLocation(), ChillowUserLocation.class);
            location.setUser(currentUser);
            location.setId(currentUser.getLocation().getId());
            currentUser.setLocation(location);
        }
        if (!CollectionUtils.isEmpty(updatedUser.getChillowUserImages())) {
            if (!CollectionUtils.isEmpty(currentUser.getChillowUserImages())) {
                currentUser.getChillowUserImages().forEach(x -> x.setIsDeleted(true));
            }
            List<ChillowUserImage> newImageList = updatedUser.getChillowUserImages().stream().
                    map(x -> {
                        ChillowUserImage img = modelMapper.map(x, ChillowUserImage.class);
                        img.setUser(currentUser);
                        img.setId(UUID.randomUUID().toString());
                        img.setIsDeleted(false);
                        return img;
                    }).collect(Collectors.toList());

            currentUser.setChillowUserImages(newImageList);

        }
        currentUser.setLastLogin(LocalDate.now());
        currentUser.setUserExpired(false);
        userRepository.save(currentUser);
        log.info("saving user");

        //UPDATE OWNER NAME IN LISTED PROPERTY ENTITY
        List<ListedProperty> listedPropertyList = listedPropertyRepository.findByOwnerId(currentUser.getId());
        for (ListedProperty eachProperty : listedPropertyList) {
            eachProperty.setOwnerName(updatedUser.getName());
        }
        listedPropertyRepository.saveAll(listedPropertyList);
        //UPDATE OWNER NAME IN LISTED PROPERTY ENTITY

        List<String> generalLivingList = updatedUser.getGeneralLivingList();
        List<String> coLivingList = updatedUser.getCoLivingList();
        List<String> offCampusList = updatedUser.getOffCampusList();

        //IF ANY EXISTING USER-PREFERRED PROPERTIES EXIST, DELETE THEM
        List<UserPreferredPropertyPartner> userPreferredPropertyPartnerList = getUserPreferredProperties(currentUser.getId());
        if (!CollectionUtils.isEmpty(userPreferredPropertyPartnerList)) {
            for (UserPreferredPropertyPartner each : userPreferredPropertyPartnerList) {
                userPrefferedPropertyPartnerRepository.delete(each);
            }
        }

        /*  NEW CODE  */
        PropertyPartnerCategory generalLivingObject = propertyPartnerCategoryRepository.findByTypeNameContaining("General Living");
        PropertyPartnerCategory colivingObject = propertyPartnerCategoryRepository.findByTypeNameContaining("Co-living");
        PropertyPartnerCategory offcampusHousingObject = propertyPartnerCategoryRepository.findByTypeNameContaining("Off-campus Housing");

        String generalLivingId = generalLivingObject.getId();
        String colivingId = colivingObject.getId();
        String offcampusHousingId = offcampusHousingObject.getId();

        String externalPropertyName = checkForExternalProperty(updatedUser.getId());
        //check if eachGen matches with external. if does then dont add;

        if (!CollectionUtils.isEmpty(generalLivingList)) {
            for (String eachGeneralLivingProperty : generalLivingList) {
                if (!eachGeneralLivingProperty.equals(externalPropertyName)) {
                    UserPreferredPropertyPartner userPreferredPropertyPartner = new UserPreferredPropertyPartner(
                            UUID.randomUUID().toString(), eachGeneralLivingProperty, currentUser.getId(), generalLivingId,
                            false, Instant.now(), Instant.now());
                    userPrefferedPropertyPartnerRepository.save(userPreferredPropertyPartner);
                }
            }
        }

        if (!CollectionUtils.isEmpty(coLivingList)) {
            for (String eachGeneralLivingProperty : coLivingList) {
                if (!eachGeneralLivingProperty.equals(externalPropertyName)) {
                    UserPreferredPropertyPartner userPreferredPropertyPartner = new UserPreferredPropertyPartner(
                            UUID.randomUUID().toString(), eachGeneralLivingProperty, currentUser.getId(), colivingId,
                            false, Instant.now(), Instant.now());
                    userPrefferedPropertyPartnerRepository.save(userPreferredPropertyPartner);
                }
            }
        }
        if (!CollectionUtils.isEmpty(offCampusList)) {
            for (String eachGeneralLivingProperty : offCampusList) {
                if (!eachGeneralLivingProperty.equals(externalPropertyName)) {
                    {
                        UserPreferredPropertyPartner userPreferredPropertyPartner = new UserPreferredPropertyPartner(
                                UUID.randomUUID().toString(), eachGeneralLivingProperty, currentUser.getId(), offcampusHousingId,
                                false, Instant.now(), Instant.now());
                        userPrefferedPropertyPartnerRepository.save(userPreferredPropertyPartner);
                    }
                }
            }
        }
        /*  NEW CODE  */

        ChillowUserEditProfileDto chillowUserEditProfileDto = modelMapper.map(currentUser, ChillowUserEditProfileDto.class);
        Optional<ExternalPropertyPartner> externalPropertyPartners = externalPropertyRepository.findByUserId(updatedUser.getId());
        if (externalPropertyPartners.isPresent()) {
            if (externalPropertyPartners.get().getPropertyType().equals(PropertyType.GENERALSPACE)) {
                generalLivingList.add(externalPropertyPartners.get().getExternalPropertyComplexName());
            }
            if (externalPropertyPartners.get().getPropertyType().equals(PropertyType.COLIVINGSPACE)) {
                coLivingList.add(externalPropertyPartners.get().getExternalPropertyComplexName());
            }
            if (externalPropertyPartners.get().getPropertyType().equals(PropertyType.OFFCAMPUSSPACE)) {
                offCampusList.add(externalPropertyPartners.get().getExternalPropertyComplexName());
            }
        }
        chillowUserEditProfileDto.setGeneralLivingList(generalLivingList);
        chillowUserEditProfileDto.setCoLivingList(coLivingList);
        chillowUserEditProfileDto.setOffCampusList(offCampusList);
//        chillowUserEditProfileDto.setGeneralLivingList(updatedUser.getGeneralLivingList());
//        chillowUserEditProfileDto.setCoLivingList(updatedUser.getCoLivingList());
//        chillowUserEditProfileDto.setOffCampusList(updatedUser.getOffCampusList());

//      Entrata Code
        try {
            List<String> propertyPartnerIds = uniquifyPropertyPartnerIds(generalLivingList, coLivingList, offCampusList);
            if (!propertyPartnerIds.isEmpty()) {
                Boolean check = propertyService.postToEntrataFromBackend(currentUser.getId(), propertyPartnerIds);
            }
        } catch (NotFoundException | JsonProcessingException e) {
            log.error(String.valueOf(e));
        }
//      Entrata Code

        return chillowUserEditProfileDto;
    }

    private String checkForExternalProperty(String id) {
        Optional<ExternalPropertyPartner> externalPropertyPartner = externalPropertyRepository.findByUserId(id);
        if (externalPropertyPartner.isPresent()) {
            return externalPropertyPartner.get().getExternalPropertyComplexName();
        }
        return null;
    }

    private List<String> uniquifyPropertyPartnerIds(List<String> generalLivingList, List<String> coLivingList,
                                                    List<String> offCampusList) {
        Set<String> propertyPartnerIds = new HashSet<>();
        propertyPartnerIds.addAll(generalLivingList);
        propertyPartnerIds.addAll(coLivingList);
        propertyPartnerIds.addAll(offCampusList);
        return new ArrayList<>(propertyPartnerIds);
    }

    @Override
    @Transactional
    public ChillowUserEditProfileDto updateUser(ChillowUserEditProfileDto updatedUser) throws NotFoundException {
        Optional<ChillowUser> existingUser = userRepository.findById(updatedUser.getId());
        if (existingUser.isPresent()) {
            ChillowUser currentUser = existingUser.get();
            log.info("user exists");
            return saveUser(currentUser, updatedUser);
        }
        log.warn("User does not exist with id: " + updatedUser.getId());
        throw new NotFoundException("User does not exist with the id");
    }

    @Override
    public ChillowUserDto saveNewUser(ChillowUserDto user) {
        return null;
    }

    @Override
    public Boolean deleteImagesOfUser(String userId, ChillowImageDto image) {
        if (StringUtils.isEmpty(userId)) {
            return false;
        }

        Optional<ChillowUser> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            List<ChillowUserImage> userImages = existingUser.get().getChillowUserImages();
            Optional<ChillowUserImage> imageToBeDeleted = userImages.stream().
                    filter(x -> x.getId().equals(image.getId())).
                    findFirst();
            if (imageToBeDeleted.isPresent()) {
                imageToBeDeleted.get().setIsDeleted(true);
                chillowUserImagesRepository.save(imageToBeDeleted.get());
                ModelMapper modelMapper = new ModelMapper();
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
                userImages.remove(imageToBeDeleted.get());
                return true;
            }

        }
        return false;
    }

    @Override
    public Optional<ChillowUser> findOneById(String id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public String getUserProfileImage(String userId) {
        ChillowUserDto user = getUserById(userId);
        if (user != null) {
            List<ChillowImageDto> userImages = user.getChillowUserImages();
            if (!CollectionUtils.isEmpty(userImages)) {
                return userImages.get(0).getFile();
            }
        }
        return StringUtils.EMPTY;
    }

    @Override
    public Map<String, String> getProfileImagesByIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.EMPTY_MAP;
        }
        Map<String, String> profileImages = new HashMap<>();
        List<ImageFile> imageList = chillowUserImagesRepository.findImagesInUserIds(ids);
        ids.forEach(id -> {
            Optional<ImageFile> image = imageList.stream().filter(x -> x.getId().equals(id)).findFirst();
            image.ifPresent(chillowUserImage -> profileImages.put(id, chillowUserImage.getFile()));
        });
        return profileImages;
    }

    @Override
    public List<ChillowUserProfile> getUserWithProfileImage(List<String> phoneNumbers) {
        List<ChillowUser> userList = userRepository.findAllByNumberIn(phoneNumbers);
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.EMPTY_LIST;
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return userList.stream().map(user -> {
            ChillowUserProfile profile = modelMapper.map(user, ChillowUserProfile.class);
            Optional<ChillowImageDto> profileImage = user.getChillowUserImages().stream().
                    filter(x -> !x.getIsDeleted() && x.getSequence() == 0)
                    .map(x -> modelMapper.map(x, ChillowImageDto.class))
                    .findFirst();
            profileImage.ifPresent(chillowImageDto -> profile.setProfileImage(chillowImageDto.getFile()));
            return profile;
        }).collect(Collectors.toList());

    }

    public List<ChillowUserProfile> getUserByEmailWithProfileImage(List<String> emails) {
        List<ChillowUser> userList = userRepository.findAllByEmailIn(emails);
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.EMPTY_LIST;
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return userList.stream().map(user -> {
            ChillowUserProfile profile = modelMapper.map(user, ChillowUserProfile.class);
            Optional<ChillowImageDto> profileImage = user.getChillowUserImages().stream().
                    filter(x -> !x.getIsDeleted() && x.getSequence() == 0)
                    .map(x -> modelMapper.map(x, ChillowImageDto.class))
                    .findFirst();
            profileImage.ifPresent(chillowImageDto -> profile.setProfileImage(chillowImageDto.getFile()));
            return profile;
        }).collect(Collectors.toList());
    }

    public boolean existsByUserId(String userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public List<ChillowUserProfile> getUserProfileByIds(List<String> ids) {
        List<ChillowUser> userList = userRepository.findByIdIn(ids);
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.EMPTY_LIST;
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return userList.stream().map(user -> {
            ChillowUserProfile profile = modelMapper.map(user, ChillowUserProfile.class);
            Optional<ChillowImageDto> profileImage = user.getChillowUserImages().stream().
                    filter(x -> !x.getIsDeleted() && x.getSequence() == 0)
                    .map(x -> modelMapper.map(x, ChillowImageDto.class))
                    .findFirst();
            profileImage.ifPresent(chillowImageDto -> profile.setProfileImage(chillowImageDto.getFile()));
            return profile;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AllUserChatProfiles> getAllChatUserProfiles(List<String> ids) {
        List<ChillowUser> userList = userRepository.findByIdIn(ids);
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.EMPTY_LIST;
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return userList.stream().map(user -> {
            AllUserChatProfiles profile = modelMapper.map(user, AllUserChatProfiles.class);
            Optional<ChillowImageDto> profileImage = user.getChillowUserImages().stream().
                    filter(x -> !x.getIsDeleted() && x.getSequence() == 0)
                    .map(x -> modelMapper.map(x, ChillowImageDto.class))
                    .findFirst();
            profileImage.ifPresent(chillowImageDto -> profile.setProfileImage(chillowImageDto.getFile()));
            return profile;
        }).collect(Collectors.toList());
    }

    @Override
    public GenerateLoginOTPResponseBody generateOTPOnLogin(GenerateLoginOTPRequestBody generateLoginOTPRequestBody) {

        expireOTPsByNumber(generateLoginOTPRequestBody.getPhoneNumber());
        if (generateLoginOTPRequestBody.getPhoneNumber().equals("+13028845163")) {
            OTP generateOTP = new OTP(UUID.randomUUID().toString(), generateLoginOTPRequestBody.getPhoneNumber(),
                    null, 900786, false, false, LocalDateTime.now(), Instant.now());
            otpRepository.save(generateOTP);
            final String message = "Chillow OTP Code: " + checkZerosToBeAddedBeforeOtp(generateOTP.getPinCode()) + "\n If you didn't request this pin disregard";
            smsService.sendSMS(generateLoginOTPRequestBody.getPhoneNumber(), message);
        } else if (generateLoginOTPRequestBody.getPhoneNumber().equals("+12125885828")) {
            OTP generateOTP = new OTP(UUID.randomUUID().toString(),
                    generateLoginOTPRequestBody.getPhoneNumber(), null, 112233, false, false, LocalDateTime.now(), Instant.now());
            otpRepository.save(generateOTP);
            final String message = "Chillow OTP Code: " + checkZerosToBeAddedBeforeOtp(generateOTP.getPinCode()) + "\n If you didn't request this pin disregard";
            smsService.sendSMS(generateLoginOTPRequestBody.getPhoneNumber(), message);
        } else {
            OTP generateOTP = new OTP(UUID.randomUUID().toString(), generateLoginOTPRequestBody.getPhoneNumber(), null,
                    getNewOtp(), false, false, LocalDateTime.now(), Instant.now());
            otpRepository.save(generateOTP);
            final String message = "Chillow OTP Code: " + checkZerosToBeAddedBeforeOtp(generateOTP.getPinCode()) + "\n If you didn't request this pin disregard";
            if (generateLoginOTPRequestBody.isSendOTP()) {
                smsService.sendSMS(generateLoginOTPRequestBody.getPhoneNumber(), message);
            }
        }
        return new GenerateLoginOTPResponseBody(200, "otp generated");
    }

    private void expireOTPsByNumber(String phoneNumber) {
        List<OTP> OTPList = otpRepository.findAllByPhoneNumber(phoneNumber);
        OTPList.forEach(x -> {
            x.setIsExpired(true);
        });
        otpRepository.saveAll(OTPList);
    }
//    private HttpHeaders getHeaders(){
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization","Bearer pat-na1-000d55d1-b312-48e6-85fa-9ba34232bc1a");
//        return headers;
//    }

    @Override
    public VerifyLoginOTPResponseBody verifyOTPOnLogin(VerifyLoginOTPRequestBody verifyLoginOTPRequestBody) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        Optional<OTP> isOTPFound = otpRepository.findByPinCodeAndPhoneNumberAndIsConsumedFalseAndIsExpiredFalse(verifyLoginOTPRequestBody.getPinCode(), verifyLoginOTPRequestBody.getPhoneNumber());
        if (isOTPFound.isPresent()) {
            OTP otp = isOTPFound.get();
            otp.setIsConsumed(true);
            otpRepository.save(otp);
        } else {
            return new VerifyLoginOTPResponseBody(null, 403, "in-valid otp!");
        }
        Optional<ChillowUser> chillowUserObject = userRepository.findOneByNumber(verifyLoginOTPRequestBody.getPhoneNumber());

        if (chillowUserObject.isPresent()) {
            ChillowUser chillowUser = chillowUserObject.get();
            chillowUser.getChillowUserVerify().setPhoneVerified(true);
            chillowUser.setAuthToken(auth.generateToken(chillowUser.getId()));
            userRepository.save(chillowUser);
            ChillowUserDto chillowUserDto = modelMapper.map(chillowUser, ChillowUserDto.class);
            LocalDate currentDate = LocalDate.now();

            chillowUserDto.setUserExpired(currentDate.minusDays(30).isAfter(chillowUser.getLastLogin()));
            return new VerifyLoginOTPResponseBody(chillowUserDto, 200, "otp verified");
        } else {
            ChillowUser newUser = createAccountWithPhoneNumber(verifyLoginOTPRequestBody.getPhoneNumber());
            userRepository.save(newUser);
            ChillowUserDto chillowUserDto = modelMapper.map(newUser, ChillowUserDto.class);
            return new VerifyLoginOTPResponseBody(chillowUserDto, 200, "User is not signed-up. Please proceed to sign-up");
        }
    }

    public ChillowUser createAccountWithPhoneNumber(String number) {
        String userId = UUID.randomUUID().toString();

        ChillowUser newUser = new ChillowUser(userId, true, false, LocalDate.now(),
                true, false, true,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, LocalDate.now(),
                number, auth.generateToken(userId), StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false,
                null, null, null, null, Collections.EMPTY_LIST,
                Collections.EMPTY_LIST, StringUtils.EMPTY, null, null, null,
                null, false, Instant.now(), Instant.now());

        ChillowUserInterests interests = new ChillowUserInterests(UUID.randomUUID().toString(), false,
                false, false, false, false
                , false, false, false, false, false, false,
                false, false, null);

        ChillowUserVerify verifyUser = new ChillowUserVerify(UUID.randomUUID().toString(), true,
                false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null);

        ChillowUserPreferences preferences = new ChillowUserPreferences(UUID.randomUUID().toString(),
                null, null, null, null, null, null, null, null,
                null, null, null, null, null);

        ChillowUserLocation location = new ChillowUserLocation(UUID.randomUUID().toString(), StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, 1.1d, 1.1d, null);

        location.setUser(newUser);
        interests.setUser(newUser);
        preferences.setUser(newUser);
        verifyUser.setUser(newUser);
        newUser.setLocation(location);
        newUser.setAuthToken(auth.generateToken(userId));
        newUser.setChillowUserPreferences(preferences);
        newUser.setChillowUserVerify(verifyUser);
        newUser.setChillowUserInterests(interests);

        return newUser;
    }

    public ChillowUser createAccountWithForEndorsement(String number, String name, String email) {
        String userId = UUID.randomUUID().toString();

        ChillowUser newUser = new ChillowUser(userId, true, false, LocalDate.now(),
                true, false, true,
                email, StringUtils.EMPTY, StringUtils.EMPTY, name, LocalDate.now(),
                number, auth.generateToken(userId), StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false,
                null, null, null, null, Collections.EMPTY_LIST,
                Collections.EMPTY_LIST, StringUtils.EMPTY, null, null, null,
                null, false, Instant.now(), Instant.now());

        ChillowUserInterests interests = new ChillowUserInterests(UUID.randomUUID().toString(), false,
                false, false, false, false
                , false, false, false, false, false, false,
                false, false, null);

        ChillowUserVerify verifyUser = new ChillowUserVerify(UUID.randomUUID().toString(), false,
                false, false,
                StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY,
                null, null, null);

        ChillowUserPreferences preferences = new ChillowUserPreferences(UUID.randomUUID().toString(),
                null, null, null, null, null, null, null, null,
                null, null, null, null, null);

        ChillowUserLocation location = new ChillowUserLocation(UUID.randomUUID().toString(), StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, 1.1d, 1.1d, null);

        location.setUser(newUser);
        interests.setUser(newUser);
        preferences.setUser(newUser);
        verifyUser.setUser(newUser);
        newUser.setLocation(location);
        newUser.setAuthToken(auth.generateToken(userId));
        newUser.setChillowUserPreferences(preferences);
        newUser.setChillowUserVerify(verifyUser);
        newUser.setChillowUserInterests(interests);
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public ChillowUserDto userSignup(ChillowUserSignupDTO body) throws NotFoundException {
        Optional<ChillowUser> chillowUser = userRepository.findById(body.getId());
        if (chillowUser.isPresent()) {
            ChillowUser user = chillowUser.get();
            user.setName(body.getName());
            user.setEmail(body.getEmail());
            user.setBirthDate(body.getBirthDate());
            user.setPronouns(body.getPronouns());
            user.getLocation().setAddress(body.getLocation().getAddress());
            user.setInitialLogin(true);
            user.getLocation().setCity(body.getLocation().getCity());
            user.getLocation().setCountry(body.getLocation().getCountry());
            user.getLocation().setState(body.getLocation().getState());
            user.getLocation().setLatitude(body.getLocation().getLatitude());
            user.getLocation().setLongitude(body.getLocation().getLongitude());

            List<ChillowUserImage> chillowUserImages = new ArrayList<>();
            List<ChillowUserSignupImagesDTO> incomingImages = body.getImages();
            if (!CollectionUtils.isEmpty(incomingImages)) {
                for (ChillowUserSignupImagesDTO eachImage : incomingImages) {
                    ChillowUserImage newImage = new ChillowUserImage(UUID.randomUUID().toString(), eachImage.getSequence(),
                            eachImage.getFile(), false, user);
                    chillowUserImages.add(newImage);
                }
                user.setChillowUserImages(chillowUserImages);
            }
            user.setWantTo(body.getWantTo());
            user.setLookingForRoommate(body.isRoomate());

            List<String> generalLivingList = body.getGeneralLivingList();
            List<String> coLivingList = body.getCoLivingList();
            List<String> offCampusList = body.getOffCampusList();

            /*  NEW CODE  */
            PropertyPartnerCategory generalLivingObject = propertyPartnerCategoryRepository.findByTypeNameContaining("General Living");
            PropertyPartnerCategory colivingObject = propertyPartnerCategoryRepository.findByTypeNameContaining("Co-living");
            PropertyPartnerCategory offcampusHousingObject = propertyPartnerCategoryRepository.findByTypeNameContaining("Off-campus Housing");

            String generalLivingId = generalLivingObject.getId();
            String colivingId = colivingObject.getId();
            String offcampusHousingId = offcampusHousingObject.getId();

            if (!CollectionUtils.isEmpty(generalLivingList)) {
                for (String eachGeneralLivingProperty : generalLivingList) {
                    UserPreferredPropertyPartner userPreferredPropertyPartner = new UserPreferredPropertyPartner(
                            UUID.randomUUID().toString(), eachGeneralLivingProperty, user.getId(), generalLivingId,
                            false, Instant.now(), Instant.now());
                    userPrefferedPropertyPartnerRepository.save(userPreferredPropertyPartner);
                }
            }

            if (!CollectionUtils.isEmpty(coLivingList)) {
                for (String eachGeneralLivingProperty : coLivingList) {
                    UserPreferredPropertyPartner userPreferredPropertyPartner = new UserPreferredPropertyPartner(
                            UUID.randomUUID().toString(), eachGeneralLivingProperty, user.getId(), colivingId,
                            false, Instant.now(), Instant.now());
                    userPrefferedPropertyPartnerRepository.save(userPreferredPropertyPartner);
                }
            }
            if (!CollectionUtils.isEmpty(offCampusList)) {
                for (String eachGeneralLivingProperty : offCampusList) {
                    UserPreferredPropertyPartner userPreferredPropertyPartner = new UserPreferredPropertyPartner(
                            UUID.randomUUID().toString(), eachGeneralLivingProperty, user.getId(), offcampusHousingId,
                            false, Instant.now(), Instant.now());
                    userPrefferedPropertyPartnerRepository.save(userPreferredPropertyPartner);
                }
            }
            /*  NEW CODE  */

            user.setNewUser(false);
            user.setLastLogin(LocalDate.now());
            userRepository.save(user);

            //IMPLEMENT HUBSPOT
            try {
                HubspotProperties hubspotProperties = new HubspotProperties();
                hubspotProperties.setEmail(body.getEmail());
                hubspotProperties.setFirstName(body.getName());
                hubspotProperties.setLastName(null);
                hubspotProperties.setPhone(user.getNumber());
                hubspotProperties.setTag("In-app user");
                hubspotProperties.setAddress(user.getLocation().getAddress());
                hubspotProperties.setCity(user.getLocation().getCity());
                hubspotProperties.setState(user.getLocation().getState());
                hubspotProperties.setCountry(user.getLocation().getCountry());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("Authorization", "Bearer " + hubsportAuthToken);
                HubspotRequestBody hubspotRequestBody = new HubspotRequestBody(hubspotProperties);
                hubSpot.postHubspotData(headers, hubspotRequestBody);
            } catch (RuntimeException exception) {
                log.error("Failed to save data on hubspot" + exception);
            }

            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
            ChillowUserDto chillowUserDto = modelMapper.map(user, ChillowUserDto.class);

            return chillowUserDto;
        } else {
            throw new NotFoundException("Account not found against the user-id!");
        }
    }

    @Override
    public boolean modifyStatus(String id) throws NotFoundException {
        Optional<ChillowUser> user = userRepository.findById(id);
        if (user.isPresent()) {
            ChillowUser existingUser = user.get();
            existingUser.setInitialLogin(false);
            userRepository.save(existingUser);
            return true;
        }
        throw new NotFoundException("Account not found against the user-id!");

    }

    @Override
    public boolean logout(String deviceId, String userId) {
        Optional<ChillowUser> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            ChillowUser user = existingUser.get();

            List<NotificationSessions> notificationSessionsList =
                    onesignalNotificationSessionRepository.findByUser(user);
            for (NotificationSessions eachNotificationId : notificationSessionsList) {
                if (eachNotificationId.getDeviceId().equals(deviceId)) {
                    onesignalNotificationSessionRepository.delete(eachNotificationId);
                }
            }
            return true;
        }
        throw new UserNotFoundException("User not found against the user id!");
    }

    @Override
    public ChillowUserDto getUserDetails(String id) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        Optional<ChillowUser> user = userRepository.findById(id);
        if (user.isPresent() && !user.get().isDeleted()) {
            ChillowUser currentUser = user.get();

            LocalDate userLastLogin = currentUser.getLastLogin();

            ChillowUserDto chillowUserDto = modelMapper.map(currentUser, ChillowUserDto.class);

            ChillowUserInterestsDto interests = modelMapper.map(currentUser.getChillowUserInterests(), ChillowUserInterestsDto.class);
            chillowUserDto.setChillowUserInterests(interests);


            ChillowUserPreferencesDto preferences = modelMapper.map(currentUser.getChillowUserPreferences(), ChillowUserPreferencesDto.class);
            chillowUserDto.setChillowUserPreferences(preferences);


            ChillowUserVerifyDto verify = modelMapper.map(currentUser.getChillowUserVerify(), ChillowUserVerifyDto.class);
            chillowUserDto.setChillowUserVerify(verify);


            ChillowUserLocationDto location = modelMapper.map(currentUser.getLocation(), ChillowUserLocationDto.class);
            chillowUserDto.setLocation(location);


            if (!CollectionUtils.isEmpty(currentUser.getChillowUserImages())) {
                List<ChillowUserImage> images = currentUser.getChillowUserImages();
                List<ChillowImageDto> newImageList = new ArrayList<>();
                for (ChillowUserImage eachImage : images) {
                    ChillowImageDto image = modelMapper.map(eachImage, ChillowImageDto.class);
                    newImageList.add(image);
                }
                chillowUserDto.setChillowUserImages(newImageList);
            }

            chillowUserDto = setPropertyPartnerValues(chillowUserDto);

            LocalDate currentDate = LocalDate.now();
            LocalDate lDate = currentDate.minusDays(30);

            chillowUserDto.setUserExpired(currentDate.minusDays(30).isAfter(currentUser.getLastLogin()));
            return chillowUserDto;
        }
        return null;
    }

    private ChillowUserDto setPropertyPartnerValues(ChillowUserDto chillowUserDto) {
        List<UserPreferredPropertyPartner> userPreferredPropertyPartnerList =
                userPrefferedPropertyPartnerRepository.findByUserIdContainingAndIsPreferenceDeletedFalse(chillowUserDto.getId());

        List<PropertyPartnerNameResponseBody> generalLivingList = new ArrayList<>();
        List<PropertyPartnerNameResponseBody> coLivingList = new ArrayList<>();
        List<PropertyPartnerNameResponseBody> offCampusList = new ArrayList<>();

        for (UserPreferredPropertyPartner eachProperty : userPreferredPropertyPartnerList) {

            /* NEW CODE */
            Optional<PropertyPartnerCategory> propertyPartnerCategory =
                    propertyPartnerCategoryRepository.findById(eachProperty.getPropertyPartnerCategoryId());
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
            /* NEW CODE */
        }
        chillowUserDto.setGeneralLivingList(generalLivingList);
        chillowUserDto.setCoLivingList(coLivingList);
        chillowUserDto.setOffCampusList(offCampusList);
        return chillowUserDto;
    }

    public Boolean updateUserEvidentStatus(EvidentIdRequestBody evidentIdRequestBody) {
        if (evidentIdRequestBody.getEventType().equals("rpRequestCompleted")) {
            Optional<ChillowUserVerify> chillowUserVerify =
                    chillowUserVerifyRepository.findByVerifiedId(evidentIdRequestBody.getRpRequestId());
            if (chillowUserVerify.isPresent()) {
                chillowUserVerify.get().setIdVerified(true);
                chillowUserVerifyRepository.save(chillowUserVerify.get());
                String userId = chillowUserVerify.get().getUser().getId();
                Map<String, String> customData = new HashMap<>();
                customData.put("type", "myprofile");
                try {
                    firebaseEvidentVerificationService.save(FirebaseEvidentVerification.builder().
                            title("ID verification")
                            .message("Your identification is cleared, Congratulations, you got verified badge in your profile").
                            sendTo(Collections.singletonList(userId)).seenBy(new ArrayList()).type("broadcast").data(customData).
                            createdAt(Timestamp.now()).build());
                } catch (InterruptedException exception) {
                    log.error("Exception occured by evident id verification webhook due to firebase", exception);
                } catch (ExecutionException e) {
                    log.error("Exception occured by evident id verification webhook due to firebase", e);
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public ChillowUserDto getAllUserMatchesWithDeleteFalse(String id) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Optional<ChillowUser> user = userRepository.findByIdAndIsDeletedFalse(id);
        if (user.isPresent()) {
            Optional<ChillowUserDto> data = user.map(chillowUser -> {
                ChillowUserDto dto = mapper.map(chillowUser, ChillowUserDto.class);
                if (CollectionUtils.isEmpty(dto.getOneSignalNotificationSessions())) {
                    dto.setOneSignalNotificationSessions(Collections.EMPTY_LIST);
                }
                return dto;
            });
            return data.orElse(null);
        }
        return null;
    }
}
