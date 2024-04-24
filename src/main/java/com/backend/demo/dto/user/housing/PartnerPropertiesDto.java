package com.backend.demo.dto.user.housing;

import com.backend.demo.enums.property.PropertyType;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PartnerPropertiesDto {
    private String id;
    private String owner;
    private String name;
    private PropertyType propertyType;
    private boolean isDeleted;
}
