package com.example.human_vs_zombies.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Player {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int player_id;

    @Column(length = 40, unique = true)
    private String biteCode;

    @Column(nullable = false)
    private boolean is_human;

    @Column(nullable = false)
    private boolean is_patient_zero;

    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToOne(mappedBy = "victim")
    private Kill death;

    @OneToMany(mappedBy = "killer")
    private Set<Kill> kills;

    @OneToOne(mappedBy = "player")
    private SquadMember squadMember;

    @OneToMany(mappedBy = "player")
    private Set<Chat> chat;

}
