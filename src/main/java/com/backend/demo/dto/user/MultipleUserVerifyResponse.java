package com.backend.demo.dto.user;

import com.backend.demo.dto.user.chillowUser.ChillowUserVerifyDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleUserVerifyResponse {
    @JsonProperty("userId")
    private String id;
    @JsonUnwrapped
    private ChillowUserVerifyDto chillowUserVerify;
}
