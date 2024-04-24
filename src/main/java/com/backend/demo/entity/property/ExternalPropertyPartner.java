package com.backend.demo.entity.property;

import com.backend.demo.enums.property.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "external_property_partner" )
public class ExternalPropertyPartner {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "external_property_complex_name")
    private String  externalPropertyComplexName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

//    @Column(name = "category")
//    private Integer category;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Enumerated(value = EnumType.STRING)
    private PropertyType propertyType;
}
