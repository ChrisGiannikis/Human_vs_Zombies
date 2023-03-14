package com.example.human_vs_zombies.dto;

import com.example.human_vs_zombies.entities.Game;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/* This DTO was created to receive a mision's data the from database */
@Getter
@Setter
public class MissionDTO {
    private int mission_id;
    private String name;
    private boolean is_human_visible;
    private boolean is_zombie_visible;
    private String description;
    private ZonedDateTime start_time;
    private ZonedDateTime end_time;
    //private Game game;
}
