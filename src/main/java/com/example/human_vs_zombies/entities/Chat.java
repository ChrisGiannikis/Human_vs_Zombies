package com.example.human_vs_zombies.entities;

import com.example.human_vs_zombies.enums.ChatScope;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Chat {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int message_id;

    @Column
    private String message;

    @Enumerated(EnumType.STRING)
    private ChatScope chatScope;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squad_id")
    private Squad squad;
}
