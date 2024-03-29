package com.example.human_vs_zombies.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class Mission {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int mission_id;

    @Column(length = 40, nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean human_visible;

    @Column(nullable = false)
    private boolean zombie_visible;

    @Column
    private String description;

    @Column
    private double lat;

    @Column
    private double lng;

    @Column
    private ZonedDateTime start_time;

    @Column
    private ZonedDateTime end_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;
}
