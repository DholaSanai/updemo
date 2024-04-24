package com.backend.demo.entity.swipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Swipe {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "shown_user_id")
    private String shownUserId;

    @Column(name = "is_liked")
    private boolean isLiked;

    @Column(name = "is_swiped_right")
    private Boolean isSwipedRight;

    @Column(name = "is_swiped_left")
    private Boolean isSwipedLeft;

    @Column(name = "deleted")
    @Where(clause = "deleted = false")
    private Boolean isDeleted;

}
