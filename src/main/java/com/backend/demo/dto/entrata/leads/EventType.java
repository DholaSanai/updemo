package com.backend.demo.dto.entrata.leads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventType {
    @JsonProperty(value = "@attributes")
    private Attribute attribute;
}
