package com.backend.demo.dto.entrata.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse {
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("code")
    private int code;
    @JsonProperty("result")
    private PropertyResult propertyResult;

}
