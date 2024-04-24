package com.backend.demo.dto.user.unsubscribeuserrequestbody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeactivateTokenRequestBody {
    private String userId;
    private String deviceToken;
}
