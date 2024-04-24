package com.backend.demo.entity.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "chillow_room_image")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChillowRoomImage {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "sequence")
    private int sequence;

    @Column(name = "file", length = 4500)
    private String file;

    @Column(name = "deleted")
    @Where(clause = "deleted = false")
    private Boolean isDeleted;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "chillow_room_id", updatable = false)
    private ChillowRoom chillowRoom;


}