package com.backend.demo.dto.user.chillowUser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ChillowUserEditProfileDto {
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

    @JsonIgnore
    private List<NotificationSessionDto> oneSignalNotificationSessions;

    private String moveInDate;
    private Integer leaseTerm;
    private Integer budgetStart;
    private Integer budgetEnd;
    private Integer wantTo;
    private boolean lookingForRoommate;
    private List<String> generalLivingList;
    private List<String> coLivingList;
    private List<String> offCampusList;
}
