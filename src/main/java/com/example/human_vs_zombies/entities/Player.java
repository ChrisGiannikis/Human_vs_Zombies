package com.example.human_vs_zombies.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "victim")
    private Kill death;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "killer")
    private Set<Kill> kills;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "player")
    private SquadMember squadMember;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private Set<Chat> chat;

}
