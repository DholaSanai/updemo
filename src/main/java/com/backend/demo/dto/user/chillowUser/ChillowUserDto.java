package com.backend.demo.dto.user.chillowUser;

import com.backend.demo.dto.property.PropertyPartnerNameResponseBody;
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
public class ChillowUserDto {
    private String id;
    private boolean isNewUser;
    private Boolean isAdmin;
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

    private List<PropertyPartnerNameResponseBody> generalLivingList;
    private List<PropertyPartnerNameResponseBody> coLivingList;
    private List<PropertyPartnerNameResponseBody> offCampusList;

    private String moveInDate;
    private Integer leaseTerm;
    private Integer budgetStart;
    private Integer budgetEnd;
    private Integer wantTo;
    private boolean lookingForRoommate;
}
