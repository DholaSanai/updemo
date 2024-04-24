package com.backend.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GoogleUser {
    private String email;
    private String name;
    @JsonProperty("email_verified")
    private Boolean emailVerified;
    private String picture;
}
