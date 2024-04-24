package com.backend.demo.entity.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Table(name = "property_partner")
public class PropertyPartner {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "complex_name")
    private String complexName; //rafigroup, sanaigroup

    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    @Column(name = "min_price")
    private String minPrice;

    @Column(name = "max_price")
    private String maxPrice;

    @Column(name = "parking_fee")
    private Float parkingFee;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "propertyPartner")
    @JoinColumn(name = "property_partner_location_id", referencedColumnName = "id")
    private PropertyPartnerLocation location;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "propertyPartner")
    @JoinColumn(name = "property_partner_amenities_id", referencedColumnName = "id")
    private PropertyPartnerAmenities propertyPartnerAmenities;

    @OneToMany(mappedBy = "propertyPartner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PropertyPartnerImage> listedPropertyImages = new ArrayList<>();

//    @OneToMany(mappedBy = "propertyPartner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<PropertyPartnerCategoryJunctionTable> propertyPartnerCategoriesList = new ArrayList<>();

    @Column(name = "is_partner_property_deleted")
    private boolean isPartnerPropertyDeleted;

    //PREFERENCES

    @Column(name = "pets")
    private Integer pets;

    @Column(name = "smoke")
    private Integer smoke;

    //PREFERENCES

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "updated_at")
    private Instant updatedAt;

}
