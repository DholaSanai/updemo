package com.backend.demo.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressValidation {
    @NotNull
    @NotBlank
    private String address;
    @NotNull
    @NotBlank
    private String city;
    @NotNull
    @NotBlank
    private String county;
    @NotNull
    @NotBlank
    private String state;
    @NotNull
    @NotBlank
    private String buildingType;
}
