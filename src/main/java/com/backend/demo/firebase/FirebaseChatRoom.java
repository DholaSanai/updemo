package com.backend.demo.firebase;

import com.google.cloud.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirebaseChatRoom {
    private String chatId;
    private String listingOwnerId;
    private String lastMsgTo;
    private Timestamp lastUpdate;
    private List<String> members;
    private Integer unreadCount;
    private String listingId;
    private Boolean isListingChat;
}

