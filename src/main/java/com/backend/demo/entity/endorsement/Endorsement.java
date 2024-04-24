package com.backend.demo.entity.endorsement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "endorsement")
public class Endorsement {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "roommate_endorsement_id")
    private String roommateEndorsementId;

    @Column(name = "given_by_user_id")
    private String givenByUserId;

    @Column(name = "given_to_user_id")
    private String givenToUserId;

    @Column(name = "is_disclose_information")
    private Boolean isDiscloseInformation;

    @Column(name = "is_endorsement_viewed")
    private Boolean isEndorsementViewed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
