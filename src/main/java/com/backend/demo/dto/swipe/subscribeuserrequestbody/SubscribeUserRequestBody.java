package com.backend.demo.dto.swipe.subscribeuserrequestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.util.UUID;
@AllArgsConstructor
@Data
@RequiredArgsConstructor
public class SubscribeUserRequestBody {
    @Valid
    @JsonProperty("uid")
    private UUID userId;
    private String deviceId;
}
