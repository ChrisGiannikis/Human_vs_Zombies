package com.example.human_vs_zombies.entities;

import com.example.human_vs_zombies.enums.Rank;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class SquadMember {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int squad_member_id;

    @Enumerated(EnumType.STRING)
    private Rank rank;

    @ManyToOne
    @JoinColumn(name = "squad_id")
    private Squad squad;

    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "squadMember")
    private Set<SquadCheckIn> squadCheckIns;
}
