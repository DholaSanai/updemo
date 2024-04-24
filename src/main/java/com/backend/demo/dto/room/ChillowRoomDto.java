package com.backend.demo.dto.room;

import com.backend.demo.enums.room.HomePreference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChillowRoomDto {
    private String id;
    @NotNull
    private String userId;
    @JsonProperty("userImage")
    private String userProfileImage;
    @JsonProperty("ownerName")
    private String userName;
    private String buildingType;
    private float monthlyRent;
    private Boolean utilitiesIncluded;
    private LocalDate whenAvailable;
    private int leaseTerm;
    private int propertySize;
    private int bedrooms;
    private int bathrooms;
    private int femaleRoommates;
    private int maleRoommates;
    private HomePreference homePreference;
    private boolean petPreference;
    private float parkingCost;
    private String roomDescription;
    @NotNull
    @JsonProperty("location")
    private ChillowRoomLocationDto chillowRoomLocation;
    @NotNull
    @JsonProperty("amenities")
    private ChillowRoomAmenitiesDto chillowRoomAmenities;
    @NotNull
    @JsonProperty("images")
    private List<ChillowRoomImageDto> chillowRoomImages;
    @JsonProperty("chats")
    private List<ChillowRoomChatDto> chillowRoomChats;
}
