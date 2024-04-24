package com.backend.demo.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Table(name = "chillow_user")
public class ChillowUser {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "is_new_user")
    private boolean isNewUser;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "last_login")
    private LocalDate lastLogin;

    @Column(name = "initial_login")
    private boolean initialLogin;

    @Column(name = "is_user_expired")
    private boolean isUserExpired;

    @Column(name = "notifications_enabled")
    private boolean notificationsEnabled;

    @Column(name = "email")
    private String email;

    @Column(name = "pronouns")
    private String pronouns;

    @Column(name = "about_me", length = 500)
    private String aboutMe;

    @Column(name = "name")
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "number", unique = true)
    private String number;

    @Column(name = "auth_token", length = 1600)
    private String authToken;

    @Column(name = "facebook_token", length = 1600)
    private String facebookToken;

    @Column(name = "google_token", length = 1600)
    private String googleToken;

    @Column(name = "apple_token", length = 1600)
    private String appleToken;

    @Column(name = "is_deleted")
    @Where(clause = "is_deleted = false")
    private boolean isDeleted;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @JoinColumn(name = "chillow_user_interests_id", referencedColumnName = "id")
    private ChillowUserInterests chillowUserInterests;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @JoinColumn(name = "chillow_user_verify_id", referencedColumnName = "id")
    private ChillowUserVerify chillowUserVerify;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @JoinColumn(name = "chillow_user_preferences_id", referencedColumnName = "id")
    private ChillowUserPreferences chillowUserPreferences;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @JoinColumn(name = "user_location_id", referencedColumnName = "id")
    private ChillowUserLocation location;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    @Where(clause = "deleted = false")
    private List<ChillowUserImage> chillowUserImages = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<NotificationSessions> notificationSessions = new ArrayList<>();

    @Column(name = "move_in_date")
    private String moveInDate;

    @Column(name = "lease_term")
    private Integer leaseTerm;

    @Column(name = "budget_start")
    private Integer budgetStart;

    @Column(name = "budget_end")
    private Integer budgetEnd;

    @Column(name = "want_to")
    private Integer wantTo;

    @Column(name = "looking_for_roommate")
    private boolean lookingForRoommate;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "updated_at")
    private Instant updatedAt;

}
