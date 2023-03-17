package com.example.human_vs_zombies.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Squad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int squad_id;

    @Column(length = 40, nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean is_human;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "squad")
    private Set<SquadMember> squadMembers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "squad")
    private Set<Chat> chat;
}
