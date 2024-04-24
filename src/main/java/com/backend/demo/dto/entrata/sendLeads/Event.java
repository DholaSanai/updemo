package com.backend.demo.dto.entrata.sendLeads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Event {
    private String type;
    private String eventTypeId;
    private String subtypeId;
    private String date;
    private String appointmentDate;
    private String timeFrom;
    private String timeTo;
    private String eventReasons;
    private String comments;
    private CallData callData;
    private EmailAddresses emailAddresses;
    private String agentId;
    private String unitSpaceIds;
    private String eventReasonId;
}
