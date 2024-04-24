package com.backend.demo.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AddRoomRequestBody {
    private String userId;
    private String buildingType;
    private float monthlyRent;
    private boolean utilitiesIncluded;
    private LocalDate whenAvailable;
    private int leaseTerm;
    private int propertySize;
    private int bedrooms;
    private int bathrooms;
    private int femaleRoommates;
    private int maleRoommates;
    private boolean smokingPreference;
    private boolean petPreference;
    private float parkingCost;
    private String roomDescription;
}
