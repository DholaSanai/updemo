package com.backend.demo.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveRoomChat {
    private String userChat;
    private String roomId;
    private String requestingUserId;
}
