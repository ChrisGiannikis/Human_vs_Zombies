package com.example.human_vs_zombies.dto.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerPostDTO {
    private int player_id;
    private String biteCode;
    private boolean is_human;
    private boolean is_patient_zero;
}
