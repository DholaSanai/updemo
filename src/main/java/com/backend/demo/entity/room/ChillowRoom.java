package com.backend.demo.entity.room;

import com.backend.demo.enums.room.HomePreference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chillow_room")
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class ChillowRoom {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "building_type")
    private String buildingType;

    @Column(name = "monthly_rent")
    private float monthlyRent;

    @Column(name = "utils_included")
    private boolean utilitiesIncluded;

    @Column(name = "when_available")
    private LocalDate whenAvailable;

    @Column(name = "lease_term")
    private int leaseTerm;

    @Column(name = "property_size")
    private int propertySize;

    @Column(name = "bedrooms")
    private int bedrooms;

    @Column(name = "bathrooms")
    private int bathrooms;

    @Column(name = "female_roommates")
    private int femaleRoommates;

    @Column(name = "male_roommates")
    private int maleRoommates;

    @Column(name = "home_preference")
    private HomePreference homePreference;

    @Column(name = "pet_preference")
    private boolean petPreference;

    @Column(name = "parking_cost")
    private float parkingCost;

    @Column(name = "room_description", length = 1000)
    private String roomDescription;

    @Column(name = "deleted")
    @Where(clause = "deleted = false")
    private Boolean isDeleted;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "chillowRoom")
    @JoinColumn(name = "chillow_room_location_id", referencedColumnName = "id")
    private ChillowRoomLocation chillowRoomLocation;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "chillowRoom")
    @JoinColumn(name = "chillow_room_amenities_id", referencedColumnName = "id")
    private ChillowRoomAmenities chillowRoomAmenities;

    @OneToMany(mappedBy = "chillowRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChillowRoomImage> chillowRoomImages = new ArrayList<>();

    @OneToMany(mappedBy = "chillowRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChillowRoomChat> chillowRoomChats = new ArrayList<>();

}
