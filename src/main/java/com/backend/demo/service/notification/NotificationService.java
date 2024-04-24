package com.backend.demo.service.notification;

import com.backend.demo.dto.property.PropertyPartnerNameResponseBody;
import com.backend.demo.dto.user.chillowUser.ChillowUserDto;
import com.backend.demo.dto.user.notificationrequestbody.BroadcastRandomNotificationRequestBody;
import com.backend.demo.dto.user.notificationresponse.CreateNotificationResponseBody;
import com.backend.demo.entity.property.ExternalPropertyPartner;
import com.backend.demo.entity.property.PropertyPartner;
import com.backend.demo.entity.property.PropertyPartnerCategory;
import com.backend.demo.entity.property.UserPreferredPropertyPartner;
import com.backend.demo.entity.user.ChillowUser;
import com.backend.demo.entity.user.ChillowUserImage;
import com.backend.demo.entity.user.NotificationSessions;
import com.backend.demo.enums.property.PropertyType;
import com.backend.demo.exceptions.MissingNotificationSessions;
import com.backend.demo.exceptions.NotificationsDisbaledException;
import com.backend.demo.exceptions.UserNotFoundException;
import com.backend.demo.firebase.FirebaseMessagingService;
import com.backend.demo.firebase.Note;
import com.backend.demo.repository.property.*;
import com.backend.demo.repository.user.OnesignalNotificationSessionRepository;
import com.backend.demo.repository.user.UserRepository;
import com.backend.demo.service.match.MatchService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationService {

    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());

    private final UserRepository repository;
    private final OnesignalNotificationSessionRepository sessionRepository;
    private final CustomNotification customNotification;

    private final FirebaseMessagingService firebaseMessagingService;
    private final MatchService swipeServiceClient;
    @Autowired
    ExternalPropertyRepository externalPropertyRepository;
    @Autowired
    private NotificationService notificationService;
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
    private UserRepository userRepository;
    @Autowired
    private PropertyPartnerLocationRepository propertyPartnerLocationRepository;
    @Autowired
    private PropertyPartnerAmenitiesRepository propertyPartnerAmenitiesRepository;

    public NotificationService(UserRepository repository, CustomNotification customNotification,
                               OnesignalNotificationSessionRepository sessionRepository,
                               FirebaseMessagingService firebaseMessagingService,
                               MatchService swipeServiceClient) {
        this.repository = repository;
        this.customNotification = customNotification;
        this.sessionRepository = sessionRepository;
        this.firebaseMessagingService = firebaseMessagingService;
        this.swipeServiceClient = swipeServiceClient;
    }


    public ChillowUserDto addDeviceForUser(String userId, String deviceId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        if (userId == null) {
            log.info("User id is null");
            return null;

        }
        Optional<ChillowUser> notificationUser = repository.findById(userId);
        if (notificationUser.isPresent()) {
            ChillowUser currentUser = notificationUser.get();
//            LocalDate minimumDate = LocalDate.now().minusDays(30);
//            boolean isExpired = false;
            LocalDate previousDate = currentUser.getLastLogin();
//            if (previousDate.isBefore(minimumDate)) {
//                isExpired = true;
//            }
            currentUser.setLastLogin(LocalDate.now());
            currentUser.setNotificationsEnabled(true);
            List<NotificationSessions> sessions = notificationUser.get().getNotificationSessions();
            Optional<NotificationSessions> alreadyExistingSession = sessions.stream().
                    filter(x -> x.getDeviceId().equals(deviceId) && x.getIsActive()).findFirst();
            if (alreadyExistingSession.isPresent()) {
                log.info("Existing session id ----> " + alreadyExistingSession.get().getDeviceId());
                log.info("Session already exists");
                repository.save(currentUser);
                ChillowUserDto chillowUserDto = modelMapper.map(currentUser, ChillowUserDto.class);

                chillowUserDto = setPropertyPartnerValues(chillowUserDto);
                LocalDate currentDate = LocalDate.now();
                LocalDate lDate = currentDate.minusDays(30);
                chillowUserDto.setUserExpired(currentDate.minusDays(30).isAfter(currentUser.getLastLogin()));
                chillowUserDto.setLastLogin(previousDate);
                return chillowUserDto;
            }
            NotificationSessions newSession = new NotificationSessions(UUID.randomUUID().toString(), deviceId, true,
                    LocalDateTime.now(), null, notificationUser.get());
            sessions.add(newSession);
            currentUser.setNotificationSessions(sessions);
            repository.save(currentUser);
            log.info("Session added for user " + userId + " device id: ---------->" + deviceId);
            ChillowUserDto chillowUserDto = modelMapper.map(currentUser, ChillowUserDto.class);
            chillowUserDto = setPropertyPartnerValues(chillowUserDto);

            LocalDate currentDate = LocalDate.now();
            chillowUserDto.setUserExpired(currentDate.minusDays(30).isAfter(currentUser.getLastLogin()));
            chillowUserDto.setLastLogin(previousDate);
            return chillowUserDto;
        }
        return null;
    }

    private ChillowUserDto setPropertyPartnerValues(ChillowUserDto chillowUserDto) {
        List<UserPreferredPropertyPartner> userPreferredPropertyPartnerList = userPrefferedPropertyPartnerRepository.findByUser(chillowUserDto.getId());

        List<PropertyPartnerNameResponseBody> generalLivingList = new ArrayList<>();
        List<PropertyPartnerNameResponseBody> coLivingList = new ArrayList<>();
        List<PropertyPartnerNameResponseBody> offCampusList = new ArrayList<>();

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
        Optional<ExternalPropertyPartner> externalPropertyPartners = externalPropertyRepository.findByUserId(chillowUserDto.getId());
        if (externalPropertyPartners.isPresent()) {
            if (externalPropertyPartners.get().getPropertyType().equals(PropertyType.GENERALSPACE)) {
                generalLivingList.add(new PropertyPartnerNameResponseBody(null, externalPropertyPartners.get().getExternalPropertyComplexName()));
            }
            if (externalPropertyPartners.get().getPropertyType().equals(PropertyType.COLIVINGSPACE)) {
                coLivingList.add(new PropertyPartnerNameResponseBody(null, externalPropertyPartners.get().getExternalPropertyComplexName()));
            }
            if (externalPropertyPartners.get().getPropertyType().equals(PropertyType.OFFCAMPUSSPACE)) {
                offCampusList.add(new PropertyPartnerNameResponseBody(null, externalPropertyPartners.get().getExternalPropertyComplexName()));
            }
        }

        chillowUserDto.setGeneralLivingList(generalLivingList);
        chillowUserDto.setCoLivingList(coLivingList);
        chillowUserDto.setOffCampusList(offCampusList);
        return chillowUserDto;
    }

    public boolean setNotificationsDisabled(String userId) throws UserNotFoundException {
        Optional<ChillowUser> user = repository.findById(userId);
        if (user.isPresent()) {
            ChillowUser data = user.get();
            data.setNotificationsEnabled(false);
            List<NotificationSessions> sessions = data.getNotificationSessions();
            sessions.forEach(s -> {
                s.setIsActive(false);
                s.setEndTime(LocalDateTime.now());
            });
            repository.save(data);
            return true;
        }
        throw new UserNotFoundException("User with id " + userId + " does not exist");
    }

    public boolean setNotificationsEnabled(String userId, String deviceId) throws UserNotFoundException {
        Optional<ChillowUser> user = repository.findById(userId);
        if (user.isPresent()) {
            ChillowUser data = user.get();
            data.setNotificationsEnabled(true);
            List<NotificationSessions> sessions = data.getNotificationSessions();
            sessions.add(new NotificationSessions(UUID.randomUUID().toString(), deviceId, true, LocalDateTime.now(), null, data));
            repository.save(data);
            return true;
        }
        throw new UserNotFoundException("User with id " + userId + " does not exist");
    }

    public ResponseEntity<CreateNotificationResponseBody> broadcastToAll(String message, Object broadcastData) {
        try {
            Map<String, Object> contents = new HashMap<>();
            contents.put("en", message);
            Map<String, Object> data = new HashMap<>();
            data.put("data", broadcastData);
            return customNotification.sendToAll(contents, data);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<CreateNotificationResponseBody> sendMessageToUser(String userId, String message, Object transferData) throws URISyntaxException,
            NotificationsDisbaledException,
            MissingNotificationSessions {
        Optional<ChillowUser> existingUser = repository.findById(userId);
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new CreateNotificationResponseBody(null, 0, null,
                    Collections.singletonList("User not subscribed or has notifications disabled")));
        } else {
            if (!existingUser.get().isNotificationsEnabled()) {
                throw new NotificationsDisbaledException("User with id " + userId + " has not enabled notifications, there notifcations can not be dispatched");
            }
        }
        Map<String, Object> contents = new HashMap<>();
        contents.put("en", message);
        Map<String, Object> data = new HashMap<>();
        data.put("data", transferData);
        List<NotificationSessions> sessions = sessionRepository.findByUser(existingUser.get());
        List<String> deviceIds = sessions.stream().filter(NotificationSessions::getIsActive).
                map(NotificationSessions::getDeviceId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(deviceIds)) {
            throw new MissingNotificationSessions("Current user has no active One signal sessions. Please enable notifications to receive notifications");
        }
        return customNotification.sendNotifcationTo(deviceIds, contents, data);
    }

    public ResponseEntity<CreateNotificationResponseBody> sendMessageToMultipleUsers(List<String> userIds, String message, Object transferData) throws URISyntaxException {
        List<ChillowUser> allUsers = repository.findByIdIn(userIds);
        if (CollectionUtils.isEmpty(allUsers)) {
            return ResponseEntity.badRequest().body(new CreateNotificationResponseBody(null, 0,
                    null, Collections.singletonList("Users not subscribed to push notifications")));
        }
        List<NotificationSessions> sessions = sessionRepository.findByUserIn(allUsers);
        Set<String> deviceIds = sessions.stream().filter(NotificationSessions::getIsActive).
                map(NotificationSessions::getDeviceId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(deviceIds)) {
            return ResponseEntity.badRequest().body(new CreateNotificationResponseBody(null, 0,
                    null, Collections.singletonList("No device ids found for user")));
        }
        Map<String, Object> contents = new HashMap<>();
        contents.put("en", message);
        Map<String, Object> data = new HashMap<>();
        data.put("data", transferData);

        return customNotification.sendNotifcationTo(new ArrayList<>(deviceIds), contents, data);
    }


    public List<String> getNotificationDeviceTokensByUserId(String userId) {
        Optional<ChillowUser> existingUser = repository.findById(userId);
        if (existingUser.isPresent()) {
            List<NotificationSessions> sessions = existingUser.get().getNotificationSessions();
            return sessions.stream().filter(NotificationSessions::getIsActive).map(NotificationSessions::getDeviceId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public boolean disableDeviceIdToken(String userId, String token) throws UserNotFoundException {
        Optional<ChillowUser> user = repository.findById(userId);
        if (user.isPresent()) {
            ChillowUser data = user.get();
            data.setNotificationsEnabled(false);
            List<NotificationSessions> sessions = data.getNotificationSessions();
            sessions.forEach(s -> {
                if (token.equals(s.getDeviceId())) {
                    s.setIsActive(false);
                    s.setEndTime(LocalDateTime.now());
                }
            });
            repository.save(data);
            log.info("Device token has been disabled for user");
            return true;
        }
        throw new UserNotFoundException("User with id " + userId + " does not exist");
    }

    @Async
    public CompletableFuture<List<NotificationSessions>> dispatchNotifications(List<NotificationSessions> sessions, String subject, String content, Map<String, String> data) {
        if (CollectionUtils.isEmpty(data)) {
            data = new HashMap<>();
            data.put("foo", "bar");
        }
        Map<String, String> finalData = data;
        sessions.forEach(session -> {
            Note note = new Note(subject, content, finalData, "Some image here");
            try {
                log.info("User device id " + session.getDeviceId());
                log.info(firebaseMessagingService.sendNotification(note, session.getDeviceId()));
            } catch (FirebaseMessagingException e) {
                log.error("Error occured while dispatching notification for id " + session.getDeviceId(), e);
                session.setEndTime(LocalDateTime.now());
                session.setIsActive(false);
            }
        });
        return CompletableFuture.supplyAsync(() -> sessions);
    }

    @Transactional
    public String sendFirebaseNotification(String receiverId, String message, Map<String, String> data) {
        Optional<ChillowUser> existingUser = repository.findById(receiverId);

        if (existingUser.isPresent()) {
            List<NotificationSessions> sessions = existingUser.get().getNotificationSessions().
                    stream().filter(NotificationSessions::getIsActive).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(sessions)) {
                log.info("User with id " + receiverId + " Does not have any session");
                return "not sent, not active sessions of user " + receiverId + " present";
            }

            try {
                CompletableFuture<List<NotificationSessions>> future = dispatchNotifications(sessions, "Chillow", message, data);
                List<NotificationSessions> updatedSessions = future.get();
                if (!CollectionUtils.isEmpty(updatedSessions)) {
                    existingUser.get().setNotificationSessions(updatedSessions);
                    repository.save(existingUser.get());
                }
                log.info("Session list has been updated");
            } catch (RuntimeException exception) {
                log.error("Error dispatching notifications ", exception);
                return "Notifcation not sent! Due to server error!";
            } catch (ExecutionException e) {
                log.error("Could not update the session list", e);
            } catch (InterruptedException e) {
                log.error("Updating sessions was interrupted", e);
            }
            log.info("Notifications sent! To sessions: " + sessions.size());
            return "sent";
        }
        return "not sent";
    }

    public String sendFirebaseMessageNotification(String senderId, String receiverId,
                                                  String message, String userChat, String listingId) {
        Optional<ChillowUser> existingUser = repository.findById(receiverId);
        Optional<ChillowUser> senderUser = repository.findById(senderId);
        if (!senderUser.isPresent()) {
            throw new UserNotFoundException("User with id " + senderId + " Does not exist");
        }
        if (existingUser.isPresent()) {
            List<NotificationSessions> sessions = existingUser.get().getNotificationSessions().
                    stream().filter(NotificationSessions::getIsActive).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(sessions)) {
                log.info("User with id " + receiverId + " Does not have any session");
                return "not sent, not active sessions of user " + receiverId + " present";
            }
            Map<String, String> data = new HashMap<>();
            data.put("chatId", userChat);
            data.put("senderName", senderUser.get().getName());
            data.put("senderId", senderUser.get().getId());
            data.put("senderEmail", senderUser.get().getEmail());
            if (listingId != null) {
                data.put("listingId", listingId);
            }
            data.put("type", "message");

            Optional<String> profileImage = senderUser.get().getChillowUserImages().stream().
                    filter(x -> x.getSequence() == 0 && !x.getIsDeleted()).
                    findFirst().map(ChillowUserImage::getFile);
            data.put("senderProfileImage", profileImage.orElse(null));
            try {
                swipeServiceClient.updateMatchMessages(userChat, senderId, 1);
            } catch (Exception exception) {
                log.error("Error occured while updating messages: ", exception);
            }
            try {
                CompletableFuture<List<NotificationSessions>> future =
                        dispatchNotifications(sessions, senderUser.get().getName(), message, data);
                List<NotificationSessions> updatedSessions = future.get();
                existingUser.get().setNotificationSessions(updatedSessions);
                repository.save(existingUser.get());
                log.info("Session list has been updated");
            } catch (RuntimeException exception) {
                log.error("Error dispatching notifications ", exception);
                return "Notifcation not sent! Due to server error!";
            } catch (ExecutionException e) {
                log.error("Could not update the session list", e);
            } catch (InterruptedException e) {
                log.error("Updating sessions was interrupted", e);
            }
            StringBuilder result = new StringBuilder("sent to: ");

            for (NotificationSessions eachSession : sessions) {
                result.append(eachSession.getDeviceId()).append(" \n");
                log.info("Notifications sent! To session: " + eachSession.getDeviceId());
            }
            return result.toString();
        }
        return "not sent";
    }

    public String sendNoMatchNotification(String senderId, String receiverId,
                                          String message, String userChat) {
        Optional<ChillowUser> existingUser = repository.findById(receiverId);
        Optional<ChillowUser> senderUser = repository.findById(senderId);
        if (!senderUser.isPresent()) {
            throw new UserNotFoundException("User with id " + senderId + " Does not exist");
        }
        if (existingUser.isPresent()) {
            List<NotificationSessions> sessions = existingUser.get().getNotificationSessions().
                    stream().filter(NotificationSessions::getIsActive).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(sessions)) {
                log.info("User with id " + receiverId + " Does not have any session");
                return "not sent, not active sessions of user " + receiverId + " present";
            }
            Map<String, String> data = new HashMap<>();
            data.put("chatId", userChat);
            data.put("senderName", senderUser.get().getName());
            data.put("senderId", senderUser.get().getId());
            data.put("senderEmail", senderUser.get().getEmail());

            data.put("type", "messageRequest");

            Optional<String> profileImage = senderUser.get().getChillowUserImages().stream().
                    filter(x -> x.getSequence() == 0 && !x.getIsDeleted()).
                    findFirst().map(ChillowUserImage::getFile);
            data.put("senderProfileImage", profileImage.orElse(null));
            try {
                swipeServiceClient.updateMatchMessages(userChat, senderId, 1);
            } catch (Exception exception) {
                log.error("Error occured while updating messages: ", exception);
            }
            try {
                CompletableFuture<List<NotificationSessions>> future =
                        dispatchNotifications(sessions, senderUser.get().getName(), message, data);
                List<NotificationSessions> updatedSessions = future.get();
                existingUser.get().setNotificationSessions(updatedSessions);
                repository.save(existingUser.get());
                log.info("Session list has been updated");
            } catch (RuntimeException exception) {
                log.error("Error dispatching notifications ", exception);
                return "Notifcation not sent! Due to server error!";
            } catch (ExecutionException e) {
                log.error("Could not update the session list", e);
            } catch (InterruptedException e) {
                log.error("Updating sessions was interrupted", e);
            }
            StringBuilder result = new StringBuilder("sent to: ");

            for (NotificationSessions eachSession : sessions) {
                result.append(eachSession.getDeviceId()).append(" \n");
                log.info("Notifications sent! To session: " + eachSession.getDeviceId());
            }
            return result.toString();
        }
        return "not sent";
    }

    public String sendNotificationOnEndorsement(String receiverId,
                                                Map<String, String> endorsements) {
        Optional<ChillowUser> existingUser = repository.findById(receiverId);

        if (existingUser.isPresent()) {
            List<NotificationSessions> sessions = existingUser.get().getNotificationSessions().
                    stream().filter(NotificationSessions::getIsActive).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(sessions)) {
                log.info("User with id " + receiverId + " Does not have any session");
                return "not sent, not active sessions of user " + receiverId + " present";
            }
            Map<String, String> data = new HashMap<>();


            data.put("type", "endorsement");
            data.put("Map", endorsements.toString());
            String message = "A former roommate said you were...";
            try {
                CompletableFuture<List<NotificationSessions>> future =
                        dispatchNotifications(sessions, "You got a new endorsement!", message, data);
                List<NotificationSessions> updatedSessions = future.get();
                existingUser.get().setNotificationSessions(updatedSessions);
                repository.save(existingUser.get());
                log.info("Session list has been updated");
            } catch (RuntimeException exception) {
                log.error("Error dispatching notifications ", exception);
                return "Notifcation not sent! Due to server error!";
            } catch (ExecutionException e) {
                log.error("Could not update the session list", e);
            } catch (InterruptedException e) {
                log.error("Updating sessions was interrupted", e);
            }
            StringBuilder result = new StringBuilder("sent to: ");

            for (NotificationSessions eachSession : sessions) {
                result.append(eachSession.getDeviceId()).append(" \n");
                log.info("Notifications sent! To session: " + eachSession.getDeviceId());
            }
            return result.toString();
        }
        return "not sent";
    }

    public Boolean broadcastRandomNotification(
            BroadcastRandomNotificationRequestBody broadcastRandomNotificationRequestBody) {
        List<ChillowUser> existingUser =
                repository.findAllByIdIn(broadcastRandomNotificationRequestBody.getUserIds());

        if (!existingUser.isEmpty()) {
            for (ChillowUser eachUser : existingUser) {
                List<NotificationSessions> sessions = eachUser.getNotificationSessions().
                        stream().filter(NotificationSessions::getIsActive).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(sessions)) {
                    log.info("User with id " + eachUser.getId() + " Does not have any session");
                } else {
                    try {
                        Map<String, String> data = new HashMap<>();
                        data.put("type", "broadcast");
                        data.put("userId", eachUser.getId());
                        data.put("message", broadcastRandomNotificationRequestBody.getMessage());
                        data.put("title", broadcastRandomNotificationRequestBody.getTitle());

                        notificationService.sendFirebaseNotificationWithCustomTitle(eachUser.getId(),
                                broadcastRandomNotificationRequestBody.getTitle(),
                                broadcastRandomNotificationRequestBody.getMessage(),
                                data);
                    } catch (Exception exception) {
                        log.warn("Error sending notifications to userId " + eachUser.getId(), exception);
                    }
                }
            }
            return true;
        }
        return null;
    }

    @Transactional
    public String sendFirebaseNotificationWithCustomTitle(String receiverId, String title, String message, Map<String, String> data) {
        Optional<ChillowUser> existingUser = repository.findById(receiverId);

        if (existingUser.isPresent()) {
            List<NotificationSessions> sessions = existingUser.get().getNotificationSessions().
                    stream().filter(NotificationSessions::getIsActive).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(sessions)) {
                log.info("User with id " + receiverId + " Does not have any session");
                return "not sent, not active sessions of user " + receiverId + " present";
            }

            try {
                CompletableFuture<List<NotificationSessions>> future = dispatchNotifications(sessions, title, message, data);
                List<NotificationSessions> updatedSessions = future.get();
                if (!CollectionUtils.isEmpty(updatedSessions)) {
                    existingUser.get().setNotificationSessions(updatedSessions);
                    repository.save(existingUser.get());
                }
                log.info("Session list has been updated");
            } catch (RuntimeException exception) {
                log.error("Error dispatching notifications ", exception);
                return "Notifcation not sent! Due to server error!";
            } catch (ExecutionException e) {
                log.error("Could not update the session list", e);
            } catch (InterruptedException e) {
                log.error("Updating sessions was interrupted", e);
            }
            log.info("Notifications sent! To sessions: " + sessions.size());
            return "sent";
        }
        return "not sent";
    }
}
