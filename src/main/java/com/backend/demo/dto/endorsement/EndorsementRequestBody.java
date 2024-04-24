package com.backend.demo.dto.endorsement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EndorsementRequestBody {
    private String name;
    private String howLong;
    private String email;
    private Boolean isDiscloseInformation;
    private String number;
    private String endorsementReceiverId;
    private Boolean trustworthy;
    private Boolean accommodating;
    private Boolean clean;
    private Boolean compassionate;
    private Boolean financiallyResponsible;
    private Boolean dependable;
    private Boolean responsible;
    private Boolean considerate;
    private Boolean kindHearted;
    private Boolean organized;
    private Boolean nightOwl;
    private Boolean earlyBird;

}
