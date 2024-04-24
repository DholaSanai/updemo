package com.backend.demo.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "chillow_user_notification_sessions")
public class NotificationSessions {
    @Id
    private String id;
    @Column(name = "device_id", nullable = false, length = 3000)
    private String deviceId;

    @Where(clause = "is_active=true")
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private ChillowUser user;

}
