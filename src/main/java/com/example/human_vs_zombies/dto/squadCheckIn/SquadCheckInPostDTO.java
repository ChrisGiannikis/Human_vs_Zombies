package com.example.human_vs_zombies.dto.squadCheckIn;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class SquadCheckInPostDTO {
    private ZonedDateTime start_time;
    private ZonedDateTime end_time;
//    private double lat;
//    private double lng;
}
