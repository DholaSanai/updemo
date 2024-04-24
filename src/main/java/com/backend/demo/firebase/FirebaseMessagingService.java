package com.backend.demo.firebase;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }


    public  class NotificationSound{
        private String sound;

        public NotificationSound(String s){
            this.sound = s;
        }
        public String getSound(){return sound;}
        public void setSound(String sound){
            this.sound = sound;
        }
    }
    public String sendNotification(Note note, String token) throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .build();
        Map<String,String> apnsHeader = new HashMap<>();
        apnsHeader.put("apns-priority","10");

        Map<String,Object> apnsPayload = new HashMap<>();
        apnsPayload.put("priority","high");
        apnsPayload.put("notification", new NotificationSound("default"));

        Aps aps = Aps.builder().setSound("default").setContentAvailable(true).build();

        ApnsConfig apnsConfig = ApnsConfig.builder().putAllHeaders(apnsHeader).putAllCustomData(apnsPayload).setAps(aps).build();

        AndroidConfig androidConfig = AndroidConfig.builder().setNotification(AndroidNotification.builder()
                .setSound("default")
                .setPriority(AndroidNotification.Priority.HIGH).build()).build();

        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .putAllData(note.getData()).setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
                .build();

        return firebaseMessaging.send(message);
    }
}
