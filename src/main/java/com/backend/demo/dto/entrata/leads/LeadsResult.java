package com.backend.demo.dto.entrata.leads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeadsResult {
    @JsonProperty("eventTypes")
    private List<EventType> eventTypeList;
}
