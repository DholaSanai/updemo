package com.backend.demo.enums.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaseTerm {
    NOT_DEFINED,
    LESS_THAN_6_MONTHS,
    MORE_THAN_6_MONTHS;

    @JsonValue
    public int toValue(){
        return ordinal();
    }
}
