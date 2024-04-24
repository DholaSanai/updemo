package com.backend.demo.dto.hubspot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
//@JsonRootName(value = "properties")
public class HubspotProperties {
    private String email;

    @JsonProperty("firstname")
    private String firstName;
    @JsonProperty("lastname")
    private String lastName;
    private String phone;
    private String tag;
    private String address;
    private String city;
    private String state;
    private String country;
}
