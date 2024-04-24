package com.backend.demo.components;

import com.backend.demo.dto.swipe.MatchDto;
import com.backend.demo.dto.user.chillowUser.ChillowImageDto;
import com.backend.demo.dto.user.chillowUser.ChillowUserDto;
import com.backend.demo.entity.swipe.Match;
import com.backend.demo.entity.swipe.Swipe;
import com.backend.demo.model.SwipeRequestBody;
import com.backend.demo.service.match.MatchService;
import com.backend.demo.service.notification.NotificationService;
import com.backend.demo.service.property.PropertyService;
import com.backend.demo.service.swipe.SwipeService;
import com.backend.demo.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@Component
@Slf4j
public class QueueConsumer {
    private static final Logger logger = Logger.getLogger(QueueConsumer.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private SwipeService swipeService;
    @Autowired
    private MatchService matchService;

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    PropertyService propertyService;

    public List<ChillowUserDto> postMatch(String userId, String shownUserId) {
        Match matched = matchService.saveNewMatch(userId, shownUserId);
        if (matched != null) {
            logger.info(Thread.currentThread().getName() + "Matched!");
            List<ChillowUserDto> users = userService.getUserByIds(Arrays.asList(userId, shownUserId));
            Optional<ChillowUserDto> userData = users.stream().filter(x -> x.getId().equals(userId)).findFirst();
            Optional<ChillowUserDto> matchedUserData = users.stream().filter(x -> x.getId().equals(shownUserId))
                    .findFirst();

            if (userData.isPresent() && matchedUserData.isPresent()) {
                swipeService.saveSwipeMatchToFirebase(true, matched.getId(), matched.getRoomOwner(),
                        userData.get(), matchedUserData.get());
            }
            return users;
        }
        return Collections.EMPTY_LIST;
    }

    public void sendNotificationOnMatch(String senderId, String receiverId, String senderName,
                                        String senderProfileImage, String chatId) {
        try {
            Map<String, String> data = new HashMap<>();
            data.put("type", "match");
            data.put("senderId", senderId);
            data.put("senderName", senderName);
            data.put("senderProfileImage", senderProfileImage);
            data.put("chatId", chatId);
            notificationService.sendFirebaseNotification(receiverId,
                    "You have a new match! Open your app to see who it is", data);
        } catch (Exception exception) {
            log.warn("Error sending notifications", exception);
        }
    }

    @Async
    public void processSwipe(SwipeRequestBody body) {
        if (body.getIsSwipedLeft()) {
            log.info("User swiped left");
            swipeService.saveNewSwipe(body.getUserId(), body.getShownUserId(), body.getIsLiked(),
                    body.getIsSwipedRight(), body.getIsSwipedLeft());
            return;
        }
        swipeService.saveNewSwipe(body.getUserId(), body.getShownUserId(), body.getIsLiked(),
                body.getIsSwipedRight(), body.getIsSwipedLeft());
        Optional<Swipe> isRightSwiped = swipeService.checkIfUserRightSwiped(body.getShownUserId(), body.getUserId());
        String chat = matchService.getUserChat(body.getUserId(), body.getShownUserId());
        String chatReverse = matchService.getUserChatReverse(body.getUserId(), body.getShownUserId());
        List<MatchDto> alreadyMatched = matchService.getSingleMatch(Arrays.asList(chat, chatReverse));

        if (isRightSwiped.isPresent()) {
            Swipe currentSwipe = isRightSwiped.get();
            if (currentSwipe.isLiked() && currentSwipe.getIsSwipedRight()
                    && body.getIsSwipedRight() && body.getIsLiked() && alreadyMatched.isEmpty()) {

                List<ChillowUserDto> users = postMatch(body.getUserId(), body.getShownUserId());

                Optional<ChillowUserDto> shownUser = users.stream().filter(x -> x.getId().equals(body.getShownUserId()))
                        .findFirst();

                if (shownUser.isPresent()) {
                    Optional<ChillowImageDto> profileImage = shownUser.get().getChillowUserImages().stream().
                            filter(x -> x.getSequence() == 0).findFirst();
                    profileImage.ifPresent(chillowImageDto ->
                            sendNotificationOnMatch(body.getUserId(), body.getShownUserId(),
                                    shownUser.get().getName(), chillowImageDto.getFile(), chat));
                }
                logger.info("Notification dispatched");
                //CALL PROPERTY FUNCTION HERE!!!!!!!!!!!!!!
                try {
                    propertyService.sendMail(body.getUserId(), body.getShownUserId());
                }catch(Exception e){
                    throw new RuntimeException(e.toString());
                }
            }
        }
    }

    @RabbitListener(queues = {"${queue.name}"}, concurrency = "1")
    public void receive(@Payload String fileBody) throws IllegalArgumentException, IOException {
        SwipeRequestBody body = objectMapper.readValue(fileBody, SwipeRequestBody.class);
        processSwipe(body);
    }
}
