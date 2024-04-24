package com.backend.demo.feignclient.user;//package com.backend.chillow.feignclient.user;
//
//import com.backend.chillow.model.EvidentIdRequestVerifyResponse;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//@FeignClient(name = "evidentId", url = "https://verify.api.demo.evidentid.com/",
//        configuration = EvidentFeignClientConfig.class)
//public interface EvidentIdClient {
//
//    @GetMapping(value = "/api/v1/verify/requests/{id}")
//    EvidentIdRequestVerifyResponse requestVerifyInfo(@PathVariable("id") String rpRequestId);
//}
