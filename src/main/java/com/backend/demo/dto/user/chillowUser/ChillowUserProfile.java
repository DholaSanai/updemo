package com.backend.demo.dto.user.chillowUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChillowUserProfile {
    String profileImage;
    private String id;
    private String email;
    private String name;
    private String birthDate;
    private String number;
}
