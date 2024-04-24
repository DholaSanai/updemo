package com.backend.demo.dto.backendVersion;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BackendVersionResponseBody {
    private UUID id;
    private String serverVersion;
    private String minimumMobileVersion;
    private Boolean isUnderMaintenance;
    private Instant createdAt;

}
