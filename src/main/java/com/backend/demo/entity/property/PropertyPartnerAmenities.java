package com.backend.demo.entity.property;

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
@Table(name = "property_partner_amenities")
public class PropertyPartnerAmenities {
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "property_partner_id", referencedColumnName = "id")
    private PropertyPartner propertyPartner;
}