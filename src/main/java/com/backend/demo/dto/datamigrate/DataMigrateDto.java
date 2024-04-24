package com.backend.demo.dto.datamigrate;

import com.backend.demo.dto.user.chillowUser.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class DataMigrateDto {
    @NotNull
    private String id;
    private boolean isNewUser;
    private boolean initialLogin;
    private boolean isUserExpired;
    private Boolean notificationsEnabled;
    private String email;
    private String pronouns;
    private String aboutMe;
    private String name;
    private LocalDate birthDate;
    private String number;
    private String authToken;
    private boolean isDeleted;
    private LocalDate lastLogin;

//    @JsonProperty("housing")
//    private ChillowHousingDto chillowHousing;

    @JsonProperty("interests")
    private ChillowUserInterestsDto chillowUserInterests;

    @JsonProperty("userVerification")
    private ChillowUserVerifyDto chillowUserVerify;

    @JsonProperty("preferences")
    private ChillowUserPreferencesDto chillowUserPreferences;

    private ChillowUserLocationDto location;

    @JsonProperty("images")
    private List<ChillowImageDto> chillowUserImages;

    private List<NotificationSessionDto> oneSignalNotificationSessions;

    private List<ListingPropertyDataMigrateDto> listedProperties;

    private String moveInDate;
    private Integer leaseTerm;
    private Integer budgetStart;
    private Integer budgetEnd;
    private Integer wantTo;
    private boolean lookingForRoommate;
    private Instant createdAt;
}
