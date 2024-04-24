package com.backend.demo.entity.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "chillow_room_location")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChillowRoomLocation {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "address")
    private String address;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "city")
    private String city;

    @Column(name = "county")
    private String county;

    @Column(name = "state")
    private String state;

    @Column(name = "longitude")
    private float longitude;

    @Column(name = "latitude")
    private float latitude;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "chillow_room_id", referencedColumnName = "id")
    private ChillowRoom chillowRoom;

}