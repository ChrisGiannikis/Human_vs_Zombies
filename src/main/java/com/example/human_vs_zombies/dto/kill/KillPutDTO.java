package com.example.human_vs_zombies.dto.kill;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class KillPutDTO {

    private int kill_id;
    private double lat;
    private double lng;
    private ZonedDateTime time_of_death;
}
