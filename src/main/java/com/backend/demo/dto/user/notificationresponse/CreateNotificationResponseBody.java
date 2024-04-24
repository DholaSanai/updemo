package com.backend.demo.dto.user.notificationresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateNotificationResponseBody {
    private String id;
    private int recipients;
    @JsonProperty("external_id")
    private String externalId;
    private List<String> errors;
}
