package com.example.human_vs_zombies.entities;

import com.example.human_vs_zombies.enums.Rank;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squad_id")
    private Squad squad;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "squadMember", fetch = FetchType.LAZY)
    private Set<SquadCheckIn> squadCheckIns;
}
