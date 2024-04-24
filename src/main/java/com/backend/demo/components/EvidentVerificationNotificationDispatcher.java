package com.backend.demo.components;

import com.backend.demo.entity.user.ChillowUser;
import com.backend.demo.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
@Component
@Slf4j
public class EvidentVerificationNotificationDispatcher {
    private static final Logger logger = Logger.getLogger(QueueConsumer.class.getName());
    @Autowired
    private NotificationService notificationService;
    public void sendNotificationOnVerification(ChillowUser user) {
        try {
            Map<String,String> data = new HashMap<>();
            data.put("type","myprofile");
            data.put("userId", user.getId());

            notificationService.sendFirebaseNotification(user.getId(),
                    "Congratulations! You have been verified.",data);
        } catch (Exception exception) {
            log.warn("Error sending notifications",exception);
        }
    }
}
