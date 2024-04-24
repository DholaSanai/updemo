package com.backend.demo.enums.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SmokePreference {
    NOT_DEFINED,
    SMOKE,
    NO_SMOKE;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
