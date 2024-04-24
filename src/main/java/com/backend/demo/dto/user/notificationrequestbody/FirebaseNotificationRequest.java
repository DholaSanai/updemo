package com.backend.demo.dto.user.notificationrequestbody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirebaseNotificationRequest {
    @NotNull
    @NotBlank
    private String receiverUserId;
    @NotNull
    private String message;
    private Map<String,String> data;
}
