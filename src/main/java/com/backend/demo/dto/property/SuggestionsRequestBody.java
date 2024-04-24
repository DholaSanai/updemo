package com.backend.demo.dto.property;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionsRequestBody {
    private String typeName;
}
