package com.backend.demo.dto.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyLocationDto {
    private String address;
    private String city;
    private String country;
    private String state;
    private Double longitude;
    private Double latitude;
}
