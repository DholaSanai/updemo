package com.backend.demo.controller.user;

import com.backend.demo.dto.user.*;
import com.backend.demo.dto.user.chillowUser.ChillowUserDto;
import com.backend.demo.dto.user.chillowUser.ChillowUserEditProfileDto;
import com.backend.demo.dto.user.chillowUser.ChillowUserProfile;
import com.backend.demo.dto.user.subscribeuserrequestbody.SubscribeUserRequestBody;
import com.backend.demo.dto.user.unsubscribeuserrequestbody.UnsubscribeUserRequestBody;
import com.backend.demo.exceptions.NullArgumentException;
import com.backend.demo.exceptions.UserNotFoundException;
import com.backend.demo.model.ChillowUserDistance;
import com.backend.demo.service.user.UserDeleteService;
import com.backend.demo.service.notification.NotificationService;
import com.backend.demo.service.user.UserService;
import javassist.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chillow-users/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDeleteService userDeleteService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ChillowUserDto getUserById(@RequestParam String id) {
        if (id == null) {
            throw new NullArgumentException("Id can of user can not be null");
        }
        return userService.getUserById(id);
    }

    @PostMapping
    public List<ChillowUserDto> getUsersByIds(@RequestBody List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }
        return userService.getUserByIds(ids);
    }

    @PostMapping("/enable-notifications")
    public boolean enableUserNotifications(@RequestBody SubscribeUserRequestBody body) {
        if (body == null)
            throw new NullArgumentException("Body data not provided");
        ChillowUserDto user = userService.getUserById(body.getUserId());
        if (user == null) {
            return false;
        }
        if (user.getNotificationsEnabled()) {
            //add the device to user
            notificationService.addDeviceForUser(user.getId(), body.getDeviceId());
            return true;
        } else {
            notificationService.setNotificationsEnabled(user.getId(), body.getDeviceId());
        }
        return true;
    }

    @PostMapping("/disable-notifications")
    public boolean disableUserNotifications(@Valid @RequestBody UnsubscribeUserRequestBody body) {
        if (body == null) {
            throw new NullArgumentException("Body can not be null");
        }
        return notificationService.setNotificationsDisabled(body.getUserId());
    }


    @PostMapping("/verify-user")
    public boolean verifyUser(@Valid @RequestBody VerifyUserRequestBody body) {
        return userService.verifyUser(body);
    }

    @PostMapping("/verify/multiple-numbers")
    public List<MultipleUserVerifyResponse> verifyMultipleUsers(@Valid @RequestBody List<String> phoneNumbers) {
        return userService.verifyMultipleUsersByPhone(phoneNumbers);
    }

    @PostMapping("/verify/phone")
    public boolean verifyUserPhone(@RequestBody VerifyByPhoneNumber body) {
        return userService.sendSMSNotificationOfOtp(body.getPhoneNumber());
    }

    @PostMapping("/verify/otp")
    public boolean verifyOtpOfPhoneNumber(@Valid @RequestBody OtpVerificationRequestBody body) {
        return userService.verifyOtpByPhoneNumber(body.getPhoneNumber(), body.getOtp());
    }

    @PostMapping("/add-images")
    public ChillowUserDto addImages(@Valid @RequestBody AddImageRequestBody body) {
        if (body == null)
            throw new NullArgumentException("Body can not be null");
        return userService.addImages(body);
    }

    @PostMapping("/update-user")
    public ChillowUserEditProfileDto updateUserProfile(@RequestBody ChillowUserEditProfileDto updatedUser) throws NotFoundException {
        if (updatedUser == null) {
            throw new RuntimeException("Request body is empty");
        }
        return userService.updateUser(updatedUser);
    }

    @PostMapping("/remove-image")
    public boolean deleteImage(@Valid @RequestBody DeleteImageDto body) {
        return userService.deleteImagesOfUser(body.getUserId(), body.getImage());
    }

    @PostMapping("/get-users-no-match")
    public List<ChillowUserDistance> getUsersNoMatch(@RequestBody GetUserNoMatchRequestBody body) {
        return userService.getUsersNotInIdsOrderedByDistance(body.getUserId(), body.getIds(),
                body.getLongitude(), body.getLatitude());
    }

    @DeleteMapping("/delete-profile")
    public boolean deleteUser(@RequestParam String id) {
        if (StringUtils.isEmpty(id)) {
            throw new UserNotFoundException("User not found, id is empty/null");
        }
        return userDeleteService.deleteUserProfile(id);
    }

    @PostMapping("/profile")
    public ResponseEntity<List<ChillowUserProfile>> getUserByPhone(@RequestBody List<String> phone) {
        if (CollectionUtils.isEmpty(phone)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.of(Optional.ofNullable(userService.getUserWithProfileImage(phone)));
    }

    @PostMapping("/profile/email")
    public ResponseEntity<List<ChillowUserProfile>> getUserByEmail(@RequestBody List<String> email) {
        if (CollectionUtils.isEmpty(email)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.of(Optional.ofNullable(userService.getUserByEmailWithProfileImage(email)));
    }

    @PostMapping("/profile/id")
    public ResponseEntity<?> getUserByIds(@RequestBody List<String> id) {
        if (CollectionUtils.isEmpty(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.of(Optional.ofNullable(userService.getAllChatUserProfiles(id)));
    }

    @GetMapping(value = "get-user-details")
    public ResponseEntity<?> getUserDetails(@RequestParam String id) throws UserNotFoundException {
        ChillowUserDto user = userService.getUserDetails(id);
        if(user != null) {
            return new ResponseEntity<>(userService.getUserDetails(id), HttpStatus.OK);
        }
        return new ResponseEntity<>("User has been deleted or does not exist!", HttpStatus.NO_CONTENT);
    }
}
