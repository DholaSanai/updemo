package com.backend.demo.dto.hubspot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class HubspotResponseBody {
    private HubspotProperties properties;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean archived;

}
