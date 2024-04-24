package com.backend.demo.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "chillow_user_interests")
@AllArgsConstructor
@NoArgsConstructor
public class ChillowUserInterests {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "foodie")
    private boolean foodie;
    @Column(name = "nature")
    private boolean nature;
    @Column(name = "technology")
    private boolean technology;
    @Column(name = "reading")
    private boolean reading;
    @Column(name = "tv")
    private boolean tv;
    @Column(name = "travel")
    private boolean travel;
    @Column(name = "video_games")
    private boolean videoGames;
    @Column(name = "music")
    private boolean music;
    @Column(name = "blogging")
    private boolean blogging;
    @Column(name = "fitness")
    private boolean fitness;
    @Column(name = "meditation")
    private boolean meditation;
    @Column(name = "volunteering")
    private boolean volunteering;
    @Column(name = "outdoor_activities")
    private boolean outdoorActivities;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private ChillowUser user;

    public void reset(){
        this.blogging = false;
        this.fitness = false;
        this.foodie = false;
        this.meditation = false;
        this.music = false;
        this.nature = false;
        this.outdoorActivities = false;
        this.reading = false;
        this.technology = false;
        this.travel = false;
        this.tv = false;
        this.videoGames = false;
        this.volunteering = false;
    }

}