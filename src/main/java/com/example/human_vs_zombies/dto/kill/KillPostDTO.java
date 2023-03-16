package com.example.human_vs_zombies.dto.kill;

import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.entities.Player;
import lombok.Data;

import java.time.ZonedDateTime;


@Data
public class KillPostDTO {

    private int id;
    private double lat;
    private double lng;

    private Player victim;
    private Player killer;

    private String biteCode;
    private ZonedDateTime time_of_death;

    private Integer game;

}
