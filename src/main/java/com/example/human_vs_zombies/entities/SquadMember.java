package com.example.human_vs_zombies.entities;

import com.example.human_vs_zombies.enums.Rank;
import com.example.human_vs_zombies.enums.State;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
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

    @OneToMany(mappedBy = "squadMember")
    private Set<SquadCheckIn> squadCheckIns;
}
