package com.backend.demo.entity.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "property_partner_location")
public class PropertyPartnerLocation {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "property_partner_id", referencedColumnName = "id")
    private PropertyPartner propertyPartner;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "updated_at")
    private Instant updatedAt;

}