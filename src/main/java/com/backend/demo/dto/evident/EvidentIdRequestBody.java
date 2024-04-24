package com.backend.demo.dto.evident;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EvidentIdRequestBody {
    private String eventType;
    private String rpRequestId;
}
