package com.backend.demo.dto.entrata.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhysicalProperty {
    @JsonProperty("Property")
    private List<Property> properties;
}
