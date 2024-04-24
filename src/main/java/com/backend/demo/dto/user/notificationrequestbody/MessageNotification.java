package com.backend.demo.dto.user.notificationrequestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageNotification {
    @NotNull
    @NotBlank
    private String senderUserId;
    @NotNull
    @NotBlank
    private String receiverUserId;
    @NotNull
    private String message;
    @JsonProperty("chatId")
    private String userChat;
    private String listingId;
}
