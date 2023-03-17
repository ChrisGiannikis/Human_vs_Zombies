package com.example.human_vs_zombies.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class Kill {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int kill_id;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;


    @Column
    private ZonedDateTime time_of_death;

    @OneToOne
    @JoinColumn(name = "victim_id")
    private Player victim;

    @ManyToOne
    @JoinColumn(name = "killer_id")
    private Player killer;
}
