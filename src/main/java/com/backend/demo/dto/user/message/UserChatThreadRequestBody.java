package com.backend.demo.dto.user.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChatThreadRequestBody {
    @NotNull
    private String userChat;
}
