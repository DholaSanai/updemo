package com.backend.demo.dto.user.apple;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppleLoginRequestBody {
    @NotNull
    private String token;
}
