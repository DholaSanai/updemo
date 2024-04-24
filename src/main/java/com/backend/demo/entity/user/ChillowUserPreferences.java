package com.backend.demo.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "chillow_user_preferences")
@AllArgsConstructor
@NoArgsConstructor
public class ChillowUserPreferences {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "pets")
    private Integer pets;
    @Column(name = "smoke")
    private Integer smoke;
    @Column(name = "drink")
    private Integer drink;
    @Column(name = "weed")
    private Integer weed;
    @Column(name = "drug")
    private Integer drug;
    @Column(name = "visitors")
    private Integer visitors;
    @Column(name = "vaccinated")
    private Integer vaccinated;
    @Column(name = "mentality")
    private Integer mentality;
    @Column(name = "quiet_hour_start")
    private String quietHourStart;
    @Column(name = "quiet_hour_end")
    private String quietHourEnd;
    @Column(name = "work_from")
    private Integer workFrom;
    @Column(name = "sleep_schedule")
    private Integer sleepSchedule;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private ChillowUser user;

}