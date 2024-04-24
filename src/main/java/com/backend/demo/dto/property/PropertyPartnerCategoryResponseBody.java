package com.backend.demo.dto.property;

import lombok.*;

import java.time.Instant;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyPartnerCategoryResponseBody {
    private String id;
    private String typeName;
    private String description;
    private Boolean isCategoryDeleted;
    private Instant createdAt;

}
