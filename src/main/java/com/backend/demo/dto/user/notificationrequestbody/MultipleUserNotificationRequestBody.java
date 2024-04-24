package com.backend.demo.dto.user.notificationrequestbody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MultipleUserNotificationRequestBody {
    @NotNull
    private List<String> userIds;
    @NotNull
    private String message;
    private Object data;
}
