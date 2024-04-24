package com.backend.demo.dto.user.google;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class GoogleLoginRequestBody {
    private String token;
}
