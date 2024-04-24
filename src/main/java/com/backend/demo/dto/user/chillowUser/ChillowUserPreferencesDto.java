package com.backend.demo.dto.user.chillowUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChillowUserPreferencesDto {
    @Min(0)
    private Integer pets;
    @Min(0)
    private Integer smoke;
    @Min(0)
    private Integer drink;
    @Min(0)
    private Integer weed;
    @Min(0)
    private Integer drug;
    @Min(0)
    private Integer visitors;
    @Min(0)
    private Integer vaccinated;
    @Min(0)
    private Integer mentality;
    @NotNull
    private String quietHourStart;
    @NotNull
    private String quietHourEnd;
    @Min(0)
    private Integer workFrom;
    @Min(0)
    private Integer sleepSchedule;
}
