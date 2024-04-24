package com.backend.demo.repository.user;

import com.backend.demo.entity.user.ChillowUser;
import com.backend.demo.entity.user.NotificationSessions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OnesignalNotificationSessionRepository extends JpaRepository<NotificationSessions, String> {
    boolean existsByDeviceId(String devceId);

    List<NotificationSessions> findByUser(ChillowUser user);

    List<NotificationSessions> findByUserIn(List<ChillowUser> ids);
}
