package com.example.human_vs_zombies.dto.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerPutDTO {
    private String biteCode;
    private boolean human;
    private boolean patient_zero;
    //    private int death; //0 for no death or the id of the kill
//    private Set<Integer> kills;
//    private Set<Chat> chat;
}
