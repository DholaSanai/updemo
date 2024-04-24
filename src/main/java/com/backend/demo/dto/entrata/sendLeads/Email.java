package com.backend.demo.dto.entrata.sendLeads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Email {
    private String optInLeadCommunication;
    private String optInMessageCenter;
    private String optInContactPoints;
    private String optInMarketingMessages;

}
