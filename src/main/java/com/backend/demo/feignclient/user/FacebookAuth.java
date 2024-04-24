package com.backend.demo.feignclient.user;

import com.backend.demo.model.FacebookUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "facebook", url = "${facebook.uri}")
public interface FacebookAuth {
    @GetMapping("/me")
    FacebookUser authenticate(@RequestParam(name = "access_token") String idToken, @RequestParam("fields") String fields);
}
