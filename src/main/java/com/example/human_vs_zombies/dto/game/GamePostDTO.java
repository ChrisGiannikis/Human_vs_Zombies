package com.example.human_vs_zombies.dto.game;

import com.example.human_vs_zombies.enums.State;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GamePostDTO {
    private String name;
    private String description;
    private State state;

    private String rules;
    private double nw_lat;
    private double nw_lng;
    private double se_lat;
    private double se_lng;
}
