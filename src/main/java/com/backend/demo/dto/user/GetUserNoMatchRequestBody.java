package com.backend.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserNoMatchRequestBody {
    String userId;
    List<String> ids;
    Double longitude;
    Double latitude;
}
