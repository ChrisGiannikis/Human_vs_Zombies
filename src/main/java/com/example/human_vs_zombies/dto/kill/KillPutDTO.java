package com.example.human_vs_zombies.dto.kill;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class KillPutDTO {

    private double lat;
    private double lng;
    private ZonedDateTime time_of_death;
}
