package com.backend.demo.entity.property;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user_preferred_property_partner")
public class UserPreferredPropertyPartner {

    @Id
    @Column(name = "id",nullable = true)
    private String id;

    @Column(name = "property_partner")
    private String propertyPartner;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "property_partner_category_id")
    private String propertyPartnerCategoryId;

    @Column(name = "is_preference_deleted")
    private Boolean isPreferenceDeleted;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "updated_at")
    private Instant updatedAt;

}


