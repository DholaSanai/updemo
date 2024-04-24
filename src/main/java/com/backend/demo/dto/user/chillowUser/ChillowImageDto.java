package com.backend.demo.dto.user.chillowUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ChillowImageDto {
    private String id;
    @NotNull
    private int sequence;
    @NotNull
    private String file;
    private Boolean isDeleted;
}
