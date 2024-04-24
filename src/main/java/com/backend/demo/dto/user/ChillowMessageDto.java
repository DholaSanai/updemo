package com.backend.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChillowMessageDto {
    private String id;
    @NotNull
    @NotBlank
    private String userChat;
    @NotNull
    @NotBlank
    private String messageHtml;
    @NotNull
    @NotBlank
    private String senderId;
    private LocalDateTime timeStamp;
    @NotNull
    private Boolean isReadByUser1;
    @NotNull
    private Boolean isReadByUser2;
}
