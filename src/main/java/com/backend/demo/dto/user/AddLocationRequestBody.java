package com.backend.demo.dto.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class AddLocationRequestBody {
    @NotNull
    private String userId;
    private String neighborhood;
    private String city;
    private String county;
    private String state;
    private Double latitude;
    private Double longitude;
}
