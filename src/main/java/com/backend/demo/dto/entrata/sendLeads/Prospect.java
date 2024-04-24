package com.backend.demo.dto.entrata.sendLeads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Prospect {
    private LeadSource leadSource;
    private String createdDate;
    private String leasingAgentId;
    private Customers customers;
    private CustomerPreferences customerPreferences;
    private Events events;
}
