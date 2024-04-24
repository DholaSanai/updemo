package com.backend.demo.controller;

import com.backend.demo.components.QueueSender;
import com.backend.demo.dto.evident.EvidentIdRequestBody;
import com.backend.demo.dto.user.*;
import com.backend.demo.dto.user.apple.AppleLoginRequestBody;
import com.backend.demo.dto.user.chillowUser.ChillowUserDto;
import com.backend.demo.dto.user.facebook.FacebookLoginRequestBody;
import com.backend.demo.dto.user.google.GoogleLoginRequestBody;
import com.backend.demo.exceptions.UserNotFoundException;
import com.backend.demo.service.entrata.EntrataService;
import com.backend.demo.service.room.ChillowRoomService;
import com.backend.demo.service.user.SmsService;
import com.backend.demo.service.user.UserService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import java.io.IOException;

import static java.util.Objects.nonNull;


@RestController
@RequestMapping("/chillow-users/api/v1/auth/")
@Slf4j
public class AuthController {

    @Autowired
    ChillowRoomService chillowRoomService;
    @Autowired
    private UserService userService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private QueueSender queueSender;
    @Autowired
    private EntrataService entrataService;

    @GetMapping("entrata-health")
    private String getit() throws IOException {
        return entrataService.checkHealthStatusEntrata();
    }

    @PostMapping("/update-evident-status")
    public Boolean updateEvidentStatus(@RequestBody EvidentIdRequestBody evidentIdRequestBody) {
        return userService.updateUserEvidentStatus(evidentIdRequestBody);
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<GenerateLoginOTPResponseBody> generateOtp(@RequestBody GenerateLoginOTPRequestBody generateLoginOTPRequestBody) {
        GenerateLoginOTPResponseBody generateLoginOTPResponseBody = userService.generateOTPOnLogin(generateLoginOTPRequestBody);
        if (generateLoginOTPResponseBody != null) {
            return new ResponseEntity<>(generateLoginOTPResponseBody, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new GenerateLoginOTPResponseBody(403, "OTP generation failed. Please retry"), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(value = "/verify-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VerifyLoginOTPResponseBody> generateOtp(@RequestBody VerifyLoginOTPRequestBody verifyLoginOTPRequestBody) {
        VerifyLoginOTPResponseBody verifyLoginOTPResponseBody = userService.verifyOTPOnLogin(verifyLoginOTPRequestBody);
        if (verifyLoginOTPResponseBody != null) {
            if (verifyLoginOTPResponseBody.getStatus() == 403) {
                return new ResponseEntity<>(new VerifyLoginOTPResponseBody(null, 403,
                        "Invalid-otp. Please retry"), HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(verifyLoginOTPResponseBody, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new VerifyLoginOTPResponseBody(null, 400,
                    "OTP verification failed. Please retry"), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> userSignup(@RequestBody ChillowUserSignupDTO body) throws NotFoundException {
        ChillowUserDto chillowUserDto = userService.userSignup(body);
        return new ResponseEntity<>(chillowUserDto, HttpStatus.OK);

    }

    @PostMapping("/modify-initial-login")
    public ResponseEntity<?> modifyInitialLogin(@RequestParam String id) throws NotFoundException {
        boolean isStatusUpdated = userService.modifyStatus(id);
        return new ResponseEntity<>(isStatusUpdated, HttpStatus.OK);
    }

    @PostMapping("/login/google")
    public ChillowUserDto googleLogin(@RequestBody GoogleLoginRequestBody body) {
        ChillowUserDto response = userService.loginWithGoogle(body.getToken());
        if (response == null) {
            throw new UsernameNotFoundException("Invalid or Expired token, user can not be logged in");
        }
        return response;
    }

    @PostMapping("/login/facebook")
    public ChillowUserDto facebookLogin(@Valid @RequestBody FacebookLoginRequestBody body) {
        return userService.loginWithFacebook(body.getToken());
    }

    @PostMapping("/login/apple")
    public ChillowUserDto appleLogin(@RequestBody AppleLoginRequestBody body) {
        return userService.loginWithApple(body.getToken());
    }

    @PostMapping(value = "/import/user")
    public String importUser(@RequestBody ChillowUserDto user) {
        if (user == null)
            throw new UserNotFoundException("No user found");
        if (StringUtils.isEmpty(user.getEmail())) {
            throw new BadRequestException("Email can not be null");
        }
        if (userService.saveNewUser(user) != null) {
            log.info("user saved");
            return "saved";
        }
        return "not saved";
    }

    @DeleteMapping("/logout")
    public boolean logout(@RequestParam String deviceId, @RequestParam String userId) {
        if (nonNull(deviceId) && nonNull(userId)) {
            return userService.logout(deviceId, userId);
        } else return false;
    }
}
