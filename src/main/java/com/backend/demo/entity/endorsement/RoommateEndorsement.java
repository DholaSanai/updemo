package com.backend.demo.entity.endorsement;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "roommate_endorsement")
public class RoommateEndorsement {

    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "how_long")
    private String howLong;
    @Column(name = "trust_worthy")
    private Boolean trustworthy;
    @Column(name = "accommodating")
    private Boolean accommodating;
    @Column(name = "clean")
    private Boolean clean;
    @Column(name = "compassionate")
    private Boolean compassionate;
    @Column(name = "financially_responsible")
    private Boolean financiallyResponsible;
    @Column(name = "dependable")
    private Boolean dependable;
    @Column(name = "responsible")
    private Boolean responsible;
    @Column(name = "considerate")
    private Boolean considerate;
    @Column(name = "kind_hearted")
    private Boolean kindHearted;
    @Column(name = "organized")
    private Boolean organized;
    @Column(name = "night_owl")
    private Boolean nightOwl;
    @Column(name = "early_bird")
    private Boolean earlyBird;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
