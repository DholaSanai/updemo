package com.backend.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class VerifyUserRequestBody {
    @NotNull
    private String userId;
    private boolean initialOverlay;
    private boolean phoneVerified;
    private boolean emailVerified;
    private boolean idVerified;
    @NotNull
    private String verifiedId;
    @NotNull
    private String verifiedIdToken;
    private boolean backgroundVerified;
    private String backgroundVerifiedId;
    private String backgroundVerifiedIdToken;
    private String smsVerificationOtp;
}
