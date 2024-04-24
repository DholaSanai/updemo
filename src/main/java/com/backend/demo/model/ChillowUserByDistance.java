package com.backend.demo.model;

import java.time.LocalDate;

public interface ChillowUserByDistance {
    String getId();
    String getEmail();
    String getName();
    String getBirthDate();
    String getNumber();
    Double getLongitude();
    Double getLatitude();
    Double getDistance();
    LocalDate getLast_Login();
}
