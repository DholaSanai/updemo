package com.backend.demo.dto.swipe;

import com.backend.demo.dto.property.PropertyPartnerNameResponseBody;
import com.backend.demo.dto.user.chillowUser.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllSwipes {
    private String id;
    private String email;
    private String pronouns;
    private String aboutMe;
    private String moveInDate;
    private boolean lookingForRoommate;
    private Integer wantTo;
    private Integer budgetStart;
    private Integer budgetEnd;
    private String name;
    private LocalDate birthDate;
    private String number;
    private Boolean isSwipedRight; //
    private Boolean isSwipedLeft; //
    private boolean isUserExpired;
    private LocalDate lastLogin;
    private ChillowUserLocationDto location;
//    @JsonProperty("housing")
//    private ChillowHousingDto chillowHousing;
    @JsonProperty("interests")
    private ChillowUserInterestsDto chillowUserInterests;
    @JsonProperty("userVerification")
    private ChillowUserVerifyDto chillowUserVerify;
    @JsonProperty("preferences")
    private ChillowUserPreferencesDto chillowUserPreferences;
    @JsonProperty("images")
    private List<ChillowImageDto> chillowUserImages;
    private List<PropertyPartnerNameResponseBody> generalLivingList;
    private List<PropertyPartnerNameResponseBody> coLivingList;
    private List<PropertyPartnerNameResponseBody> offCampusList;
}
