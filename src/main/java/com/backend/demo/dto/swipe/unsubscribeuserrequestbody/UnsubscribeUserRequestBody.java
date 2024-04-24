package com.backend.demo.dto.swipe.unsubscribeuserrequestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UnsubscribeUserRequestBody {
    @JsonProperty("uid")
    private UUID userId;
}
