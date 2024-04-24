package com.backend.demo.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FacebookPictureData {
    private Integer height;
    private String url;
    private Integer width;
}
