package com.backend.demo.dto.entrata.sharedClasses;

import com.backend.demo.dto.entrata.sendLeads.Prospects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Params {
    private Integer showAllStatus;
    private String propertyId;
    private String doNotSendConfirmationEmail;
    private String isWaitList;
    private Prospects prospects;
}
