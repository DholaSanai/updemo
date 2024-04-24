package com.backend.demo.dto.swipe;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OneSignalNotificationSessionDto {
    private UUID id;
    private String deviceId;
    private Boolean isActive;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
