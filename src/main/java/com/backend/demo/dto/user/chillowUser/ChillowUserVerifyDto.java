package com.backend.demo.dto.user.chillowUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChillowUserVerifyDto {
    private boolean phoneVerified;
    private String paymentSource;
    private String paymentVerificationId;
    private String purchaseId;
    private LocalDateTime paymentDateTime;
    private Boolean isPaymentDone;
    private boolean emailVerified;
    private boolean idVerified;
    private String verifiedId;
    private String verifiedIdToken;

}
