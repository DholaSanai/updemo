package com.backend.demo.entity.user;

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
@Table(name = "user_property_junction_table")
public class UserPropertyJunctionTable {

        @Id
        @Column(name = "id", nullable = false)
        private String id;

        @Column(name = "user_id")
        private String userId;

        @Column(name = "listed_property_id")
        private String listedPropertyId;

        @Column(name = "is_listed_property_deleted")
        private boolean isListedPropertyDeleted;

        @CreationTimestamp
        @Column(name = "created_at")
        private Instant createdAt;

        @LastModifiedBy
        @Column(name = "updated_at")
        private Instant updatedAt;
}
