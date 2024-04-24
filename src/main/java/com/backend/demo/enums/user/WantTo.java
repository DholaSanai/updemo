package com.backend.demo.enums.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WantTo {
    POST_A_PLACE,
    FIND_A_PLACE;
    @JsonValue
    public int toValue(){
        return ordinal();
    }
}
