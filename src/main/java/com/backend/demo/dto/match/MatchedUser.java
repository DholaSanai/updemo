package com.backend.demo.dto.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MatchedUser {
    private String id;
    private String chillowUserImage;
    private String name;
    private String pronouns;
    private LocalDate birthDate;
    private String chatId;
}
