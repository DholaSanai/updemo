package com.backend.demo.dto.property;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyPartnerRequestBody {
    private List<String> propertyPartnerCategories; //PARTNER PROPERTY CAN HAVE MULTIPLE CATEGORIES
    private String complexName;
    private String description;
    private String minPrice;
    private String maxPrice;
    private Float parkingFee;
    private PropertyLocationDto location;
    private PropertyAmenitiesDto amenities;
    private List<PropertyImageDto> image;  //ONLY SINGLE IMAGE IS SHOWN ON FRONTEND
    private boolean isPartnerPropertyDeleted;
    private Integer pets;
    private Integer smoke;
    private String email; //for property partner management
}