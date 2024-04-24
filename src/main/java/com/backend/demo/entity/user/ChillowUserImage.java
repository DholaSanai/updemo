package com.backend.demo.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "chillow_user_image")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChillowUserImage {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "sequence")
    private int sequence;

    @Column(name = "file", length = 7200)
    private String file;

    @Column(name = "deleted")
    @Where(clause = "deleted = false")
    private Boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = true, updatable = false)
    private ChillowUser user;


}