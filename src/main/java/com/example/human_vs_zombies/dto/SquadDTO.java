package com.example.human_vs_zombies.dto;

import lombok.Data;

@Data
public class SquadDTO {

    private int squad_id;

    private String name;

    private boolean is_human;

    private int game;
}
