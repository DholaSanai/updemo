package com.backend.demo.dto.property.featured;

import com.backend.demo.dto.property.ListedPropertyResponseBody;
import com.backend.demo.dto.property.PropertyAmenitiesDto;
import com.backend.demo.dto.property.PropertyImageDto;
import com.backend.demo.dto.property.PropertyLocationDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeaturedPartners {
    private List<String> propertyPartnerCategories; //PARTNER PROPERTY CAN HAVE MULTIPLE CATEGORIES
    private String id;
    private String complexName;
    private String description;
    private String minPrice;
    private String maxPrice;
    private Float parkingFee;
    private PropertyLocationDto location;
    private PropertyAmenitiesDto amenities;
    private List<PropertyImageDto> image;//ONLY SINGLE IMAGE IS SHOWN ON FRONTEND
    private boolean isPartnerPropertyDeleted;
    private Integer pets;
    private Integer smoke;
    @JsonProperty(value = "available_listings")
    private List<ListedPropertyResponseBody> listedPropertyResponseBodies;
}
