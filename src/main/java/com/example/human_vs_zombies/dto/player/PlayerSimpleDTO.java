package com.example.human_vs_zombies.dto.player;

import com.example.human_vs_zombies.entities.AppUser;
import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.entities.SquadMember;
import lombok.Getter;
import lombok.Setter;

/* This DTO created to get not sensitive player details */
@Getter
@Setter
public class PlayerSimpleDTO {
    private int player_id;
    private AppUser user;
    private Kill death;
    private SquadMember squadMember;
}
