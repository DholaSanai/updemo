package com.backend.demo.dto.swipe.notificationrequestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateNotificationRequestBody {
    @JsonProperty("app_id")
    private String appId;
    @JsonProperty("included_segments")
    private List<String> includedSegments;
    private Map<String, Object> data;
    private Map<String, Object> contents;
}
