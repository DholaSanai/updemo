package com.backend.demo.dto.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MatchedSpecificInfo {
    private String chatId;
    private String matchedUserId;
}
