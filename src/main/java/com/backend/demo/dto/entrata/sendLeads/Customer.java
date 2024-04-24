package com.backend.demo.dto.entrata.sendLeads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Customer {
    private Name name;
    private String customerRelationshipTypeId;
    private Address address;
    private Phone phone;
    private String email;
    private MarketingPreferences marketingPreferences;

}
