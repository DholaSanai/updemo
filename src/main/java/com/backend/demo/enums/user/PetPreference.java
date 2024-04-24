package com.backend.demo.enums.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PetPreference {
    NOT_DEFINED,
    MAYBE,
    I_LOVE_PETS,
    NO_THANK_YOU;
    @JsonValue
    public int toValue(){
        return ordinal();
    }
}
