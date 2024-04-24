package com.backend.demo.dto.contest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ContestRequestBody {
    private String userId;
    private String story;
    private List<String> images;
}
