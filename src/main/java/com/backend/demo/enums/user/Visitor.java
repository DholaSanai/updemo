package com.backend.demo.enums.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Visitor {
    NOT_DEFINED,
    ANYTIME_IS_FINE,
    DAY_TIME_ONLY,
    OVERNIGHT_IS_COOL,
    NO_THANK_YOU;
    @JsonValue
    public int toValue(){
        return ordinal();
    }
}
