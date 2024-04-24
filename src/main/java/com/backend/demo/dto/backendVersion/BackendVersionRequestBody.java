package com.backend.demo.dto.backendVersion;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BackendVersionRequestBody {

    private UUID id;
    private String serverVersion;
    private String minimumMobileVersion;
    private Boolean isUnderMaintenance;
}
