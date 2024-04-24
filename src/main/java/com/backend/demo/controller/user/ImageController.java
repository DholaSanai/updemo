package com.backend.demo.controller.user;

import com.backend.demo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chillow-users/api/v1/images")
public class ImageController {
    @Autowired
    private UserService userService;

    @GetMapping("/user-profile")
    public String getUserProfileImage(@RequestParam String userId) {
        return userService.getUserProfileImage(userId);
    }

    @PostMapping("/list/profile-image")
    public Map<String, String> getProfileImagesOfUsers(@RequestBody List<String> ids) {
        return userService.getProfileImagesByIds(ids);
    }

}
