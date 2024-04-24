package com.backend.demo.dto.user.chillowUser;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationSessionDto {
    private String id;
    private String deviceId;
    private Boolean isActive;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
