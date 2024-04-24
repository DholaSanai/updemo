package com.backend.demo.dto.user;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChillowUserSignupImagesDTO {
    private String file;
    private int sequence;

}
