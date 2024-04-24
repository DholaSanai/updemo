package com.backend.demo.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Table(name = "chillow_message")
public class ChillowMessage {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "user_chat")
    private String userChat;

    @Column(name = "message_html", length = 5000)
    private String messageHtml;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "timestamp")
    private LocalDateTime timeStamp;

    @Column(name = "is_read_by_user_1")
    private Boolean isReadByUser1;

    @Column(name = "is_read_by_user_2")
    private Boolean isReadByUser2;
}
