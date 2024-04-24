package com.backend.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class GenerateLoginOTPRequestBody {
    private String phoneNumber;
    private boolean sendOTP;
}
