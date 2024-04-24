package com.backend.demo.dto.property;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyPartnerNameResponseBody {
    private String id;
    private String complexName;

}
