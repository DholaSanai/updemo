package com.backend.demo.enums.room;

import com.fasterxml.jackson.annotation.JsonValue;

public enum HomePreference {
    NOT_DEFINED,
    SMOKE,
    PET_FRIENDLY;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}