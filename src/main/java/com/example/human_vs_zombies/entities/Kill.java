package com.example.human_vs_zombies.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
public class Kill {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int kill_id;

    @Column
    private double lat;

    @Column
    private double lng;

    @Column
    private ZonedDateTime time_of_death;

    @OneToOne
    @JoinColumn(name = "victim_id")
    private Player player;

}