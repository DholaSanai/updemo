package com.backend.demo.dto.entrata.sharedClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Method {
    private String name;
    private String version;
    private Params params;
}
