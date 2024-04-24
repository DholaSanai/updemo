package com.backend.demo.controller.user;

import com.backend.demo.dto.GeneralResponseBody;
import com.backend.demo.dto.user.broadcast.BroadcastRequestBody;
import com.backend.demo.dto.user.chillowUser.ChillowUserDto;
import com.backend.demo.dto.user.notificationrequestbody.*;
import com.backend.demo.dto.user.notificationresponse.CreateNotificationResponseBody;
import com.backend.demo.dto.user.subscribeuserrequestbody.SubscribeUserRequestBody;
import com.backend.demo.dto.user.unsubscribeuserrequestbody.DeactivateTokenRequestBody;
import com.backend.demo.exceptions.NotificationsDisbaledException;
import com.backend.demo.exceptions.UserNotFoundException;
import com.backend.demo.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/chillow-users/api/v1/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/device/add-token")
    public ResponseEntity<?> addDeviceForNotifications(@RequestBody SubscribeUserRequestBody body) throws UserNotFoundException {
        ChillowUserDto chillowUserDto = notificationService.addDeviceForUser(body.getUserId(), body.getDeviceId());
        if(chillowUserDto != null){
            return new ResponseEntity<>(chillowUserDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(new GeneralResponseBody("in-valid user id", 403),HttpStatus.FORBIDDEN);
    }

    @PostMapping("/device/broadcast-random-notification")
    public ResponseEntity<?> broadcastRandomNotification(@RequestBody BroadcastRandomNotificationRequestBody broadcastRandomNotificationRequestBody) throws UserNotFoundException {
        if(notificationService.broadcastRandomNotification(broadcastRandomNotificationRequestBody) == true ||
                notificationService.broadcastRandomNotification(broadcastRandomNotificationRequestBody) == false){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/device/tokens")
    public List<String> getDeviceTokensOfUser(@RequestParam String userId) throws UserNotFoundException {
        return notificationService.getNotificationDeviceTokensByUserId(userId);
    }

    @PutMapping("/device/token/deactivate")
    public boolean deactivateUserDeviceToken(@RequestBody DeactivateTokenRequestBody body) throws UserNotFoundException {
        return notificationService.disableDeviceIdToken(body.getUserId(), body.getDeviceToken());
    }

    @PostMapping("/send")
    String sendNotificationWithFirebase(@RequestBody FirebaseNotificationRequest body) {
        return notificationService.sendFirebaseNotification(body.getReceiverUserId(),
                body.getMessage(), body.getData());
    }


    @PostMapping("/send/message-notification")
    public String sendMessageNotification(@RequestBody MessageNotification body) {
        return notificationService.sendFirebaseMessageNotification(body.getSenderUserId(), body.getReceiverUserId()
                , body.getMessage(), body.getUserChat(), body.getListingId());
    }

    //DEPRECIATE THE APIS BELOW
    @PostMapping("/broadcast")
    public ResponseEntity<CreateNotificationResponseBody> broadCastToAll(@RequestBody BroadcastRequestBody body) {
        return notificationService.broadcastToAll(body.getMessage(), body.getData());
    }

    @PostMapping("/send-to-client")
    public ResponseEntity<CreateNotificationResponseBody> sendToClient(@RequestBody UserNotificationRequestBody body)
            throws URISyntaxException, NotificationsDisbaledException {
        return notificationService.sendMessageToUser(body.getUserId(), body.getMessage(), body.getData());
    }

    @PostMapping("/send-to-multiple-clients")
    public ResponseEntity<CreateNotificationResponseBody> sendMessageToMultipleUsers(@RequestBody MultipleUserNotificationRequestBody body)
            throws URISyntaxException {
        return notificationService.sendMessageToMultipleUsers(body.getUserIds(), body.getMessage(), body.getData());
    }
}
