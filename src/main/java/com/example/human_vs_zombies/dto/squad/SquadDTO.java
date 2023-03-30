package com.example.human_vs_zombies.dto.squad;

import lombok.Data;

@Data
public class SquadDTO {

    private int squad_id;

    private String name;

    private boolean human;

    private int game;

    private int active_members;

    private int deceased_members;
}
