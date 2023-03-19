package com.example.human_vs_zombies.dto.kill;


import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class KillDTO {
    private int kill_id;
    private double lat;
    private double lng;


<<<<<<< HEAD
    private Integer killer;
    private Integer victim;
    private Integer game;

=======
    private int killer;
    private int victim;
>>>>>>> f243b00c0efc9f51f6e419fde69d0c22dd586d8f
    private ZonedDateTime time_of_death;

}
