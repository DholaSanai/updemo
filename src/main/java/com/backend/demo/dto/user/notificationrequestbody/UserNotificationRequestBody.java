package com.backend.demo.dto.user.notificationrequestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserNotificationRequestBody {
    @JsonProperty("uid")
    @NotNull
    private String userId;
    @NotNull
    private String message;
    private Object data;
}
