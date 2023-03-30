package com.example.human_vs_zombies.dto.player;
import com.example.human_vs_zombies.entities.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/* This DTO created only for admins to get players details */
@Getter
@Setter
public class PlayerSimpleDTO {
    private int player_id;
    private String biteCode;

}
