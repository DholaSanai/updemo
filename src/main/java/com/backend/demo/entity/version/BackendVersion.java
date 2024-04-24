package com.backend.demo.entity.version;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "backend_version")
public class BackendVersion {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "server_version", nullable = false)
    private String serverVersion;

    @Column(name = "minimum_mobile_version", nullable = false)
    private String minimumMobileVersion;

    @Column(name = "is_under_maintenance")
    private Boolean isUnderMaintenance;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "updated_at")
    private Instant updatedAt;
}
