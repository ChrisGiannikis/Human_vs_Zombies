package com.example.human_vs_zombies.dto.kill;


import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.entities.Player;
import lombok.Data;;

import java.time.ZonedDateTime;

@Data
public class KillDTO {
    private int id;
    private double lat;
    private double lng;


    //private Player killer;
  //  private Player victim;
//    private Integer game;

    private ZonedDateTime time_of_death;

}
