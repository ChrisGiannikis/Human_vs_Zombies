package com.example.human_vs_zombies.dto.player;

import lombok.Data;

/* This DTO created to get not sensitive player details */
@Data
public class PlayerDTO {
    private int player_id;
    private String biteCode;
    private boolean human;
    private boolean patient_zero;
    private int user;
    private int game;
}
