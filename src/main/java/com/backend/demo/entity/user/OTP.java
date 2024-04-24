package com.backend.demo.entity.user;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "otp")
public class OTP {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Nullable
    @Column(name = "pin_code")
    private int pinCode;

    @Column(name = "is_consumed")
    private Boolean isConsumed;

    @Column(name = "is_expired")
    private Boolean isExpired;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
