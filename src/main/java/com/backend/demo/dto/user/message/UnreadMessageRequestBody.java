package com.backend.demo.dto.user.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnreadMessageRequestBody {
    @NotNull
    @NotBlank
    private String userChat;
    @NotNull
    @NotBlank
    private String userId;
    @NotNull
    @NotBlank
    private String currentUserId;
    @NotBlank
    @NotNull
    private String matchedUserId;
}
