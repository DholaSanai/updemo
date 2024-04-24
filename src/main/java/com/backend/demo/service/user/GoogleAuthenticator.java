package com.backend.demo.service.user;

import com.backend.demo.feignclient.user.GoogleAuth;
import com.backend.demo.model.GoogleUser;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleAuthenticator {
    @Value("${oauth2.google.clientId}")
    private String clientId;

    @Value("${oauth2.google.clientSecret}")
    private String clientSecret;

    @Autowired
    private GoogleAuth googleAuth;

    public GoogleUser authenticateWithGoogle(String token) throws FeignException {
        return googleAuth.authenticate(token);
    }
}
