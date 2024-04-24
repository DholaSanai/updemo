package com.backend.demo.dto.swipe.notificationrequestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserNotificationRequestBody {
    @JsonProperty("uid")
    @NotNull
    private UUID userId;
    @NotNull
    private String message;
    private Object data;
}
