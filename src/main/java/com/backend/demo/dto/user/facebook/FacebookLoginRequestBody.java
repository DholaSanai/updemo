package com.backend.demo.dto.user.facebook;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class FacebookLoginRequestBody {
    @NotNull
    private String token;
}
