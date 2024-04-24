package com.backend.demo.dto.entrata.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Property {
    @JsonProperty("PropertyID")
    private int propertyId;
    @JsonProperty("MarketingName")
    private String marketingName;
    @JsonProperty("PropertyLookupCode")
    private String propertyLookupCode;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("General_ID")
    private String generalId;
    @JsonProperty("YearBuilt")
    private String yearBuilt;
    @JsonProperty("webSite")
    private String website;
    @JsonProperty("Address")
    private Address address;
}
