package com.backend.demo.dto.entrata.admin;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendLeadRequestBody {
    private String firstName;
    private String lastName;
    private String number;
    private String email;
    private String propertyPartnerId;
}
