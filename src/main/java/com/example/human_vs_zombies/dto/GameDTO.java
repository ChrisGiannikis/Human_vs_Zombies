package com.example.human_vs_zombies.dto;

import com.example.human_vs_zombies.enums.State;
import lombok.Data;

@Data
public class GameDTO {
    private int game_id;
    private String name;
    private String description;
    private State state;

    private String rules;
    private double nw_lat;
    private double nw_lng;
    private double se_lat;
    private double se_lng;

}
