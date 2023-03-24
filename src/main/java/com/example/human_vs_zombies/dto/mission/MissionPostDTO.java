package com.example.human_vs_zombies.dto.mission;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class MissionPostDTO {
    private String name;
    private boolean human_visible;
    private boolean zombie_visible;
    private String description;
    private ZonedDateTime start_time;
    private ZonedDateTime end_time;
    private int game;

}
