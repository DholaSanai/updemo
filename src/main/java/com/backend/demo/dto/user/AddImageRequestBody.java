package com.backend.demo.dto.user;

import com.backend.demo.dto.user.chillowUser.ChillowImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddImageRequestBody {
    private String userId;
    private List<ChillowImageDto> images;
}
