package com.backend.demo.dto.swipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchDto {
    private UUID id;
    private String userId;
    private String matchedUserId;
    private String userChat;
    private String roomOwner;
    private boolean isDeleted;
}
