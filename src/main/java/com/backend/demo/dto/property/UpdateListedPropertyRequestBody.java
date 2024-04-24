package com.backend.demo.dto.property;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateListedPropertyRequestBody {
    private String id;
    private Integer buildingType;
    private Boolean isStudentHousing;
    private Boolean isCoLiving;
    private String complexName;
    private String partnerId;  //IF COMPLEX NAME IS SELECTED FROM SUGGESTION
    private Integer monthlyRent;
    private Boolean isUtilitiesIncluded;
    private LocalDate whenAvailable;
    private Integer leaseTerm;
    private Integer propertySizeSqFt;
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer femaleRoommates;
    private Integer maleRoommates;
    private Boolean isCurrentlyLivingHere;
    private Float parkingFee;
    private String roomDescription;
    private PropertyLocationDto location;
    private PropertyAmenitiesDto amenities;
    private Integer pets;
    private Integer smoke;
    private List<PropertyImageDto> images;

}
