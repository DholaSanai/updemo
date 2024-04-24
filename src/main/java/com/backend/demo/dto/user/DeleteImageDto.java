package com.backend.demo.dto.user;

import com.backend.demo.dto.user.chillowUser.ChillowImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class DeleteImageDto {
    @NotNull
    @NotBlank
    private String userId;

    @NotNull
    private ChillowImageDto image;

}
