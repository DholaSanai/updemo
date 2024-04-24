package com.backend.demo.dto.datamigrate;

import com.backend.demo.dto.user.chillowUser.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class DataMigrateDtoResponseBody {
    @NotNull(message = "id can not be null")
    private String id;

    private boolean isNewUser;
    private boolean initialLogin;
    private boolean isUserExpired;
    private Boolean notificationsEnabled;
    private String email;
    private String pronouns;
    private String aboutMe;
    @NotEmpty(message = "Name can not be null")
    private String name;


    private LocalDate birthDate;
    private String number;
    private String authToken;
    private boolean isDeleted;
    private LocalDate lastLogin;

//    @JsonProperty("housing")
//    private ChillowHousingDto chillowHousing;
    @NotNull(message = "interests can not be null")
    @JsonProperty("interests")
    private ChillowUserInterestsDto chillowUserInterests;


    @JsonProperty("userVerification")
    private ChillowUserVerifyDto chillowUserVerify;
    @NotNull(message = "preferences can not be null")
    @JsonProperty("preferences")
    private ChillowUserPreferencesDto chillowUserPreferences;
    @NotNull(message = "location can not be null")
    private ChillowUserLocationDto location;

    @JsonProperty("images")
    private List<ChillowImageDto> chillowUserImages;

    @NotNull(message = "listedProperties must not be null, it can be empty")
    private List<ListingPropertyDataMigrateDto> listedProperties;

    @NotNull
    private String moveInDate;
    @Min(0)
    @NotNull(message = "lease term must not be null")
    private Integer leaseTerm;
    @Min(0)
    private Integer budgetStart;
    @Min(1)
    private Integer budgetEnd;
    @Range(min = 0, max=1)
    @NotNull
    private Integer wantTo;
    private boolean lookingForRoommate;
    private Instant createdAt;
}
