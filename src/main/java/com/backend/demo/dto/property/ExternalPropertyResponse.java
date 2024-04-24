package com.backend.demo.dto.property;

import com.backend.demo.enums.property.PropertyType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ExternalPropertyResponse {
    private String externalPropertyComplexName;
    private String neighborhood;
    private String city;
    private String state;
    private String country;
    private PropertyType propertyType;
}
