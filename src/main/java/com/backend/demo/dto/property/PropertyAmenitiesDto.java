package com.backend.demo.dto.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PropertyAmenitiesDto {
    private boolean parking;
    private boolean privateEntrance;
    private boolean gym;
    private boolean adaAccessible;
    private boolean doorMan;
    private boolean pool;
    private boolean privateBathroom;
    private boolean wifi;
    private boolean ac;
    private boolean privateLaundry;
    private boolean furnished;
    private boolean privateCloset;

}