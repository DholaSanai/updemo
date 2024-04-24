package com.backend.demo.dto.user.subscribeuserrequestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@RequiredArgsConstructor
public class SubscribeUserRequestBody {
    @NotNull
    @NotBlank
    private String userId;

    @JsonProperty("deviceToken")
    private String deviceId;
}
