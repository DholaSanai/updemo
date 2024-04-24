package com.backend.demo.dto.user.chillowUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChillowUserInterestsDto {
    private Boolean foodie;
    private Boolean nature;
    private Boolean technology;
    private Boolean reading;
    private Boolean tv;
    private Boolean travel;
    private Boolean videoGames;
    private Boolean music;
    private Boolean blogging;
    private Boolean fitness;
    private Boolean meditation;
    private Boolean volunteering;
    private Boolean outdoorActivities;
}
