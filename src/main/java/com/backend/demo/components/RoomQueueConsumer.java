package com.backend.demo.components;

import com.backend.demo.dto.room.UsersInRadiusOfRoom;
import com.backend.demo.dto.user.notificationrequestbody.FirebaseNotificationRequest;
import com.backend.demo.entity.property.ListedProperty;
import com.backend.demo.repository.room.ChillowRoomRepository;
import com.backend.demo.service.notification.NotificationService;
import com.backend.demo.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RoomQueueConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ChillowRoomRepository roomRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;


    @Async
    public void processNotificationDispatchToUsers(ListedProperty listedProperty, String userId) {

        List<UsersInRadiusOfRoom> users = roomRepository.findUsersWithinRadius(listedProperty.getLocation().getLongitude(),
                listedProperty.getLocation().getLatitude(), userId);

        if (!CollectionUtils.isEmpty(users)) {
            users.forEach(user -> {
                Map<String, String> data = new HashMap<>();
                data.put("listingId", listedProperty.getId());
                data.put("type", "listing");
                data.put("userId", user.getId());
                FirebaseNotificationRequest request = new FirebaseNotificationRequest(user.getId(),
                        " A new Location has been added in your area", data);
                try {
                    String response = notificationService.sendFirebaseNotification(request.getReceiverUserId(),
                            request.getMessage(), request.getData());
                    log.info("Notification status from API: " + response);
                } catch (Exception e) {
                    log.error("Error occured! Request to FeignClient API failed", e);
                }
            });
        }
    }

    @RabbitListener(queues = {"${room.queue.name}"}, concurrency = "1")
    public void receive(@Payload String fileBody) throws IllegalArgumentException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        ListedProperty room = mapper.readValue(fileBody, ListedProperty.class);
        processNotificationDispatchToUsers(room,room.getOwnerId());
        log.info("Room queue received, " + fileBody);
    }
}
