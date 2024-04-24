package com.backend.demo.dto.entrata.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @JsonProperty("@attributes")
    private AddressAttributes attributes;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("City")
    private String city;
    @JsonProperty("State")
    private String state;
    @JsonProperty("PostalCode")
    private String postalCode;
    @JsonProperty("Country")
    private String country;
    @JsonProperty("Email")
    private String email;
}
