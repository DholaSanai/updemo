package com.backend.demo.entity.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "listing_property")
public class ListedProperty {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "is_listing_deleted")
    private Boolean isListingDeleted;

    @Column(name = "is_added_by_admin")
    private Boolean isAddedByAdmin;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "building_type")
    private Integer buildingType; //apartment, townhouse, room, house

    @Column(name = "is_student_housing")
    private Boolean isStudentHousing;

    @Column(name = "is_coliving")
    private Boolean isCoLiving;

    @Column(name = "complex_name")
    private String complexName; //rafigroup, sanaigroup

    @Column(name = "property_partner_id")
    private String propertyPartnerId; //WHEN A NORMAL USER SELECTS REGISTERED COMPLEX NAME

    @Column(name = "monthly_rent")
    private Integer monthlyRent;

    @Column(name = "is_utilities_included")
    private Boolean isUtilitiesIncluded;

    @Column(name = "when_available")
    private LocalDate whenAvailable;

    @Column(name = "lease_term")
    private Integer leaseTerm;

    @Column(name = "property_size_sqft")
    private Integer propertySizeSqFt;

    @Column(name = "bedrooms")
    private Integer bedrooms;

    @Column(name = "bathrooms")
    private Integer bathrooms;

    @Column(name = "female_roommates")
    private Integer femaleRoommates;

    @Column(name = "male_roommates")
    private Integer maleRoommates;

    @Column(name = "is_currently_living_here")
    private Boolean isCurrentlyLivingHere;

    @Column(name = "parking_fee")
    private Float parkingFee;

    @Column(name = "room_description", length = 10000)
    private String roomDescription;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "listedProperty")
    @JoinColumn(name = "listed_property_location_id", referencedColumnName = "id")
    private ListedPropertyLocation location;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "listedProperty")
    @JoinColumn(name = "listed_property_amenities_id", referencedColumnName = "id")
    private ListedPropertyAmenities listedPropertyAmenities;


    @OneToMany(mappedBy = "listedProperty", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ListedPropertyImage> listedPropertyImages = new ArrayList<>();

    //PREFERENCES

    @Column(name = "pets")
    private Integer pets;

    @Column(name = "smoke")
    private Integer smoke;

    //PREFERENCES

    @Column(name = "is_update_required")
    private Boolean isUpdateRequired;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "updated_at")
    private Instant updatedAt;

}
