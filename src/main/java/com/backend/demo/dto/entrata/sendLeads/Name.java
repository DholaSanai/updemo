package com.backend.demo.dto.entrata.sendLeads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Name {
    private String firstName;
    private String lastName;
    private String namePrefix;
    private String middleName;
    private String maidenName;
    private String nameSuffix;

}
