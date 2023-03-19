package com.example.human_vs_zombies.dto.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerPostDTO {
    private String biteCode;
    private boolean is_human;
    private boolean is_patient_zero;
    private int user;
    private int game;
//    private int death; //0 for no death or the id of the kill
//    private Set<Integer> kills;
    private int squadMember;
//    private Set<Chat> chat;
}
