package com.example.human_vs_zombies.dto.squad;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SquadPutDTO {
    private int squad_id;

    private String name;

    private boolean is_human;

    private int game;
}
