package com.backend.demo.enums.property;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum PropertyType {
    GENERALSPACE,
    COLIVINGSPACE,
    OFFCAMPUSSPACE;


    @JsonValue
    public int toValue() {
        return ordinal();
    }
}

