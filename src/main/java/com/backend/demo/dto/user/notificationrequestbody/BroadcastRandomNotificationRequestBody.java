package com.backend.demo.dto.user.notificationrequestbody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BroadcastRandomNotificationRequestBody {
    private List<String> userIds;
    private String title;
    private String message;
}
