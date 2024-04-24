package com.backend.demo.entity.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chillow_room_chat")
@Setter
@Getter
public class ChillowRoomChat {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "chat")
    private String chat;

    @Column(name = "profile_image", length = 3000)
    private String profileImage;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "chillow_room_id", updatable = false)
    private ChillowRoom chillowRoom;
}
