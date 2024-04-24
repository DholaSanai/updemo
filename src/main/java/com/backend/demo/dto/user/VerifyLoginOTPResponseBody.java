package com.backend.demo.dto.user;

import com.backend.demo.dto.user.chillowUser.ChillowUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class VerifyLoginOTPResponseBody {
    private ChillowUserDto chillowUser;
    private int status;
    private String message;
}
