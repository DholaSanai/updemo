package com.backend.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerificationRequestBody {
    @NotNull
    private String phoneNumber;
    @NotNull
    private Integer otp;
}
