package com.backend.demo.dto.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExternalPropertyPartnerRequestBody {

    @NotNull(message = "Please Enter Complex Name")
    private String externalPropertyComplexName;

    private String userId;
    @NotNull(message = "Neighborhood Can't Be Null")
    private String neighborhood;

    @NotNull(message = "City Name Can't Be Null")
    private String city;

    @NotNull(message = "State Name Can't Be Null")
    private String state;

    @NotNull(message = "Country Name Can't Be Null")
    private String country;

    @NotNull(message = "Latitude Name Can't Be Null")
    private Double latitude;

    @NotNull(message = "Longitude Name Can't Be Null")
    private Double longitude;

    private Integer category;
}
