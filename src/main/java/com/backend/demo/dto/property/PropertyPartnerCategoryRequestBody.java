package com.backend.demo.dto.property;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyPartnerCategoryRequestBody {
    private String id;
    private String typeName;
    private String description;
    private boolean isCategoryDeleted;
}
