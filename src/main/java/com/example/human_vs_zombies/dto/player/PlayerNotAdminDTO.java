package com.example.human_vs_zombies.dto.player;

import lombok.Getter;
import lombok.Setter;

/* This DTO created only for admins to get players details */
@Getter
@Setter
public class PlayerNotAdminDTO {
    private int player_id;
    private String biteCode;
    private boolean human;
    private int user;
    private int game;
    private String full_name;
}
