package com.backend.demo.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MessageResponseBody {

    private String id;
    private String requestedById;
    private String image;
    private String name;
    private String pronouns;
    private LocalDate dateOfBirth;
    private String message;
    private LocalDateTime sendDateTime;

}
