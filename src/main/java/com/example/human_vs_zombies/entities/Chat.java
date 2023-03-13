package com.example.human_vs_zombies.entities;

import com.example.human_vs_zombies.enums.ChatScope;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Chat {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int message_id;

    @Column
    private String message;

    @Enumerated(EnumType.STRING)
    private ChatScope chatScope;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "squad_id")
    private Squad squad;
}
