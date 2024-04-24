package com.backend.demo.dto.endorsement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AllEndorsementResponseBody {
    private int received;
    private int given;
    private EndorsementCount yourEndorsements;
    private EndorsementCount givenEndorsements;
}
