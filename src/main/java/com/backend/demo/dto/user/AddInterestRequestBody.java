package com.backend.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AddInterestRequestBody {
    @NotNull
    private String userId;
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
