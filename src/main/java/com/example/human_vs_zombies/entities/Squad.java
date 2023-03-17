package com.example.human_vs_zombies.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "squad")
    private Set<SquadMember> squadMembers;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "squad")
    private Set<Chat> chat;
}
