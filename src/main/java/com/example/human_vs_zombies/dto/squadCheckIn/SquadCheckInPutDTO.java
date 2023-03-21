package com.example.human_vs_zombies.dto.squadCheckIn;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class SquadCheckInPutDTO {
    private int squad_checkin_id;
    private ZonedDateTime start_time;
    private ZonedDateTime end_time;
    private double lat;
    private double lng;
    private Integer squadMember;
}
