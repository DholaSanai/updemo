package com.backend.demo.dto.user.chillowUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChillowUserLocationDto {
    private String address;
    private String city;
    private String country;
    private String state;
    private Double latitude;
    private Double longitude;
}
