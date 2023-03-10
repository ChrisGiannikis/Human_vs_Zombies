package com.example.human_vs_zombies.entities;

import com.example.human_vs_zombies.enums.State;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int game_id;

    @Column(length = 40, nullable = false)
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column
    private String rules;

    @Column
    private double nw_lat;

    @Column
    private double nw_lng;

    @Column
    private double se_lat;

    @Column
    private double se_lng;

    @OneToMany(mappedBy = "game")
    private Set<Player> players;

    @OneToMany(mappedBy = "game")
    private Set<Mission> missions;

    @OneToMany(mappedBy = "game")
    private Set<Squad> squads;
}
