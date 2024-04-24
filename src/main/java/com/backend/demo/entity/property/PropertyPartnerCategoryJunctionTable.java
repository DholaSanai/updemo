package com.backend.demo.entity.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.time.Instant;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Table(name = "property_partner_category_junction_table")
public class PropertyPartnerCategoryJunctionTable {

        @Id
        @Column(name = "id", nullable = false)
        private String id;

        @Column(name = "property_category_id")
        private String propertyCategoryId;

        @Column(name = "property_partner_id")
        private String propertyPartnerId; //ONE PROPERTY CAN HAVE MULTIPLE CATEGORIES

        @Column(name = "is_listed_property_deleted")
        private boolean isListedPropertyDeleted;

//        @ManyToOne
//        @JoinColumn(name = "property_partner_id")
//        private PropertyPartner propertyPartner;

        @CreationTimestamp
        @Column(name = "created_at")
        private Instant createdAt;

        @LastModifiedBy
        @Column(name = "updated_at")
        private Instant updatedAt;


}
