package com.backend.demo.dto.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyImageDto {
    private int sequence;
    private String file;
    private Boolean isDeleted;
}