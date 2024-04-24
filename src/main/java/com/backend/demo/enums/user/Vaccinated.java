package com.backend.demo.enums.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Vaccinated {
    NOT_DEFINED,
    VACCINATED,
    NOT_VACCINATED;

    @JsonValue
    public int toValue(){
        return ordinal();
    }
}
