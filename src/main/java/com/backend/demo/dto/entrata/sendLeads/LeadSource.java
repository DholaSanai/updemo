package com.backend.demo.dto.entrata.sendLeads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LeadSource {
    private String originatingLeadSourceId;
    private String additionalLeadSourceIds;

}
