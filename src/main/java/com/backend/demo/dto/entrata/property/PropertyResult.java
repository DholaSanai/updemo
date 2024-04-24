package com.backend.demo.dto.entrata.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResult {
    @JsonProperty("PhysicalProperty")
    private PhysicalProperty physicalProperty;
}
