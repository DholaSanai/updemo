package com.backend.demo.service.notification;

import com.backend.demo.dto.user.notificationresponse.CreateNotificationResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface CustomNotification {
    ResponseEntity<CreateNotificationResponseBody> sendToAll(Map<String, Object> contents, Map<String, Object> data) throws URISyntaxException, RestClientException;

    ResponseEntity<CreateNotificationResponseBody> sendNotifcationTo(List<String> recipients, Map<String, Object> contents, Map<String, Object> data) throws URISyntaxException, RestClientException;
}
