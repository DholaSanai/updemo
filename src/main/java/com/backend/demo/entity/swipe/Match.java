package com.backend.demo.entity.swipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Match {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "matched_user_id")
    private String matchedUserId;

    @Column(name = "user_id_messages")
    private Integer userIdMessages;

    @Column(name = "matched_user_id_messages")
    private Integer matchedUserIdMessages;

    @Column(name = "user_chat", length = 255)
    private String userChat;

    @Column(name = "room_owner")
    private String roomOwner;

    @Column(name = "deleted")
    @Where(clause = "deleted = false")
    private Boolean isDeleted;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
