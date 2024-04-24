package com.backend.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllUserChatProfiles {
    private String profileImage;
    private String id;
    private String email;
    private String name;
    private String birthDate;
    private String number;
    private boolean isDeleted;
}
