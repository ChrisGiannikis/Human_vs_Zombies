package com.example.human_vs_zombies.dto.kill;


import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class KillDTO {
    private int kill_id;
    private double lat;
    private double lng;
    private int killer;
    private int victim;

    private ZonedDateTime time_of_death;

}
