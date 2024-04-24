package com.backend.demo.dto.entrata.sendLeads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Phone {
    private String personalPhoneNumber;
    private String cellPhoneNumber;
    private String officePhoneNumber;
    private String faxNumber;

}
