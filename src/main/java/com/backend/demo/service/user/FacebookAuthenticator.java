package com.backend.demo.service.user;

import com.backend.demo.feignclient.user.FacebookAuth;
import com.backend.demo.model.FacebookUser;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacebookAuthenticator {

    private final String fields = "name,email";
    @Autowired
    private FacebookAuth facebookAuth;

    public FacebookUser facebookAuthentication(String idToken) throws FeignException {
        return facebookAuth.authenticate(idToken, fields);
    }
}
