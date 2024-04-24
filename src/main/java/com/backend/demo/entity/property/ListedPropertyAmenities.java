package com.backend.demo.entity.property;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "listed_property_amenities")
public class ListedPropertyAmenities {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "parking")
    private boolean parking;

    @Column(name = "private_entrance")
    private boolean privateEntrance;

    @Column(name = "gym")
    private boolean gym;

    @Column(name = "ada_accessible")
    private boolean adaAccessible;

    @Column(name = "door_man")
    private boolean doorMan;

    @Column(name = "pool")
    private boolean pool;

    @Column(name = "private_bathroom")
    private boolean privateBathroom;

    @Column(name = "wifi")
    private boolean wifi;

    @Column(name = "ac")
    private boolean ac;

    @Column(name = "private_laundry")
    private boolean privateLaundry;

    @Column(name = "furnished")
    private boolean furnished;

    @Column(name = "private_closet")
    private boolean privateCloset;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "listed_property_id", referencedColumnName = "id")
    private ListedProperty listedProperty;
}