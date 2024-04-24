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
public class MessageRequestBody {
    private String id;
    private String requestedById;
    private String requestedToId;
    private LocalDate dateOfBirth;
    private String message;
    private LocalDateTime sendDateTime;
    private Boolean isAccepted;

}
