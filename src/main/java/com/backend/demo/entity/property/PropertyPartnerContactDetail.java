package com.backend.demo.entity.property;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "property_partner_contact_detail")
public class PropertyPartnerContactDetail {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "property_partner")
    private String propertyPartner;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
