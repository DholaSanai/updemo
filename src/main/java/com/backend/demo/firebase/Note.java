package com.backend.demo.firebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    private String subject;
    private String content;
    private Map<String, String> data;
    private String image;
}
