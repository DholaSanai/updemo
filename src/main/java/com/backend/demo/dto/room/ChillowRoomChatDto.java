package com.backend.demo.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChillowRoomChatDto {
    private String id;
    private String chat;
    private String profileImage;
}
