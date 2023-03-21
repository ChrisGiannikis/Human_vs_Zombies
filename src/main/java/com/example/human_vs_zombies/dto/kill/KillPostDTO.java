package com.example.human_vs_zombies.dto.kill;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;


@Getter
@Setter
public class KillPostDTO {

    private double lat;
    private double lng;

    private int victim;
    private int killer;

//    private String biteCode;
    private ZonedDateTime time_of_death;

//    private Integer game;

}
