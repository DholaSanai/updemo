package com.backend.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class GetUserMatchRequestBody {
    private String userId;
    private List<String> ids;
    private Double longitude;
    private Double latitude;
}
