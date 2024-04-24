package com.backend.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChillowUserDistance {
    private String id;
    private String email;
    private String name;
    private String birthDate;
    private String number;
    private Double longitude;
    private Double latitude;
    private Double distance;
}
