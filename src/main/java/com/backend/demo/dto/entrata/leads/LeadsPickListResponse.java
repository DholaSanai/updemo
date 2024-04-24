package com.backend.demo.dto.entrata.leads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeadsPickListResponse {
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("code")
    private int code;
    @JsonProperty("result")
    private LeadsResult leadsResult;

}
