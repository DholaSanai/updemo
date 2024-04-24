package com.backend.demo.entity.message;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "message")
public class Message {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "requested_by_id")
    private String requestedById;

    @Column(name = "requested_to_id")
    private String requestedToId;

    @Column(name = "is_request_accepted")
    private Boolean isRequestAccepted;

    @Column(name = "message",length = 100000)
    private String message;

    @Column(name = "send_date_time")
    private LocalDateTime sendDateTime;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "updated_at")
    private Instant updatedAt;

}
