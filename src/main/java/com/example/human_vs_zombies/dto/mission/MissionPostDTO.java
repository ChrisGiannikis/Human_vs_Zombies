package com.example.human_vs_zombies.dto.mission;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class MissionPostDTO {
    private int mission_id;
    private String name;
    private boolean is_human_visible;
    private boolean is_zombie_visible;
    private String description;
    private ZonedDateTime start_time;
    private ZonedDateTime end_time;
}
