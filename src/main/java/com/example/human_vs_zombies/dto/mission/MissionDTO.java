package com.example.human_vs_zombies.dto.mission;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/* This DTO was created to receive a mision's data the from database */
@Getter
@Setter
public class MissionDTO {
    private int mission_id;
    private String name;
    private boolean human_visible;
    private boolean zombie_visible;
    private String description;
    private double lat;
    private double lng;
    private ZonedDateTime start_time;
    private ZonedDateTime end_time;
    private int game;
}
