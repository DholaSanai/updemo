package com.backend.demo.service.notification;


import com.backend.demo.dto.user.notificationrequestbody.CreateNotificationRequestBody;
import com.backend.demo.dto.user.notificationrequestbody.UserSpecificNotificationRequestBody;
import com.backend.demo.dto.user.notificationresponse.CreateNotificationResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CustomNotificationImpl implements CustomNotification {
    private static final Logger logger = Logger.getLogger(CustomNotificationImpl.class.getName());
    @Value("${onesignal.uri}")
    private String NOTIFICATIONS_URL;
    @Value("${onesignal.app_id}")
    private String appId;
    @Value("${onesignal.rest_api_key}")
    private String restApiKey;

    public ResponseEntity<CreateNotificationResponseBody> dispatchNotification(CreateNotificationRequestBody body)
            throws URISyntaxException, RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        URI url = new URI(NOTIFICATIONS_URL);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        headers.set("Authorization", "Basic " + restApiKey);
        HttpEntity<CreateNotificationRequestBody> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, CreateNotificationResponseBody.class);
    }

    public ResponseEntity<CreateNotificationResponseBody> dispatchNotification(UserSpecificNotificationRequestBody body)
            throws URISyntaxException, RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        URI url = new URI(NOTIFICATIONS_URL);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        headers.set("Authorization", "Basic " + restApiKey);
        HttpEntity<UserSpecificNotificationRequestBody> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, CreateNotificationResponseBody.class);
    }

    @Override
    public ResponseEntity<CreateNotificationResponseBody> sendToAll(Map<String, Object> contents, Map<String, Object> data)
            throws URISyntaxException, RestClientException {
        CreateNotificationRequestBody body = new CreateNotificationRequestBody(appId,
                Collections.singletonList("Active Users"), data, contents);
        return dispatchNotification(body);
    }

    @Override
    public ResponseEntity<CreateNotificationResponseBody> sendNotifcationTo(List<String> recipients, Map<String, Object> contents, Map<String, Object> data)
            throws URISyntaxException, RestClientException {
        UserSpecificNotificationRequestBody body = new UserSpecificNotificationRequestBody(appId,
                recipients, data, contents);
        return dispatchNotification(body);
    }
}
