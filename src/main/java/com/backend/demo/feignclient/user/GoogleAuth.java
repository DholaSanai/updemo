package com.backend.demo.feignclient.user;


import com.backend.demo.model.GoogleUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleclient", url = "${google.uri}")
public interface GoogleAuth {
    @GetMapping(value = "/tokeninfo")
    GoogleUser authenticate(@RequestParam(name = "id_token") String idToken);
}
