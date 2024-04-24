package com.backend.demo.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChillowRoomLocationDto {
    private String address;
    private String neighborhood;
    private String city;
    private String county;
    private String state;
    private float longitude;
    private float latitude;
}
