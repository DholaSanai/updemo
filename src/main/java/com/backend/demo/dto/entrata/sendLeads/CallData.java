package com.backend.demo.dto.entrata.sendLeads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CallData {
    private String callFrom;
    private String ringThrough;
    private String callStatus;
    private String duration;
    private String audioLink;
}
