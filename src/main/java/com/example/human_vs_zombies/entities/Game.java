package com.example.human_vs_zombies.entities;

import com.example.human_vs_zombies.enums.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Entity
@Getter
@Setter
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game", fetch = FetchType.LAZY)
    private Set<Player> players;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game", fetch = FetchType.LAZY)
    private Set<Mission> missions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game", fetch = FetchType.LAZY)
    private Set<Squad> squads;

    @Override
    public String toString(){
        return name + " " + description + " " + rules + " " + nw_lat + " " + nw_lng + " " + se_lat + " " + se_lng;
    }
}
