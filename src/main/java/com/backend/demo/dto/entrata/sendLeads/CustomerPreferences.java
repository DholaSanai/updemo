package com.backend.demo.dto.entrata.sendLeads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CustomerPreferences {
    private String desiredMoveInDate;
    private String desiredFloorplanId;
    private String desiredUnitTypeId;
    private String desiredUnitId;
    private Rent desiredRent;
    private String desiredNumBedrooms;
    private String desiredNumBathrooms;
    private String desiredLeaseTerms;
    private String desiredLeaseTermId;
    private int numberOfOccupants;
    private String comment;
}
