package com.backend.demo.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChillowRoomAmenitiesDto {
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
