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
public class UserSpecificNotificationRequestBody {
    @JsonProperty("app_id")
    private String appId;
    @JsonProperty("include_player_ids")
    private List<String> includePlayerIds;
    private Map<String, Object> data;
    private Map<String, Object> contents;
}
