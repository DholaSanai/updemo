package com.backend.demo.dto.datamigrate;

import com.backend.demo.dto.property.PropertyAmenitiesDto;
import com.backend.demo.dto.property.PropertyImageDto;
import com.backend.demo.dto.property.PropertyLocationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ListingPropertyDataMigrateDto {
    private String ownerId;
    private Boolean isListingDeleted;
    private String building;
    private Boolean isStudentHousing;
    private Boolean isCoLiving;
    private String complexName;
    private String partnerId;  //IF COMPLEX NAME IS SELECTED FROM SUGGESTION
    @NotNull
    @Min(200)
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
