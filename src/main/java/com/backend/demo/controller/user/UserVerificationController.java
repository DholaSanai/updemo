package com.backend.demo.controller.user;

import com.backend.demo.dto.user.chillowUser.ChillowUserVerifyDto;
import com.backend.demo.service.user.UserService;
import com.backend.demo.service.user.UserVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chillow-users/api/v1/user-verify")
public class UserVerificationController {

    @Autowired
    private UserVerificationService userVerificationService;

    @Autowired
    private UserService userService;


    @PutMapping("/background/{userId}")
    public ChillowUserVerifyDto verifyUserBackgroundData(@PathVariable("userId") String userId, @RequestParam String token) {
        if (userService.existsByUserId(userId)) {
            return userVerificationService.setBackgroundVerificationToken(userId, token);
        }
        throw new UsernameNotFoundException("User does not exist");

    }

    @PutMapping("/id/{userId}")
    public ChillowUserVerifyDto verifyUserData(@PathVariable("userId") String userId, @RequestParam String token) {
        if (userService.existsByUserId(userId)) {
            return userVerificationService.setIdVerificationToken(userId, token);
        }
        throw new UsernameNotFoundException("User does not exist");
    }

    @GetMapping("/check")
    public String checkVerificationOfUser(@RequestParam String token) {
        return userVerificationService.receiveVerification(token);
    }
}
