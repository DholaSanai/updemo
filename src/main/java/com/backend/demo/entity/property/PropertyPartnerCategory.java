package com.backend.demo.entity.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Table(name = "property_partner_category")
public class PropertyPartnerCategory {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "description")
    private String description;

    @Column(name = "is_category_deleted")
    private boolean isCategoryDeleted;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "updated_at")
    private Instant updatedAt;

}
