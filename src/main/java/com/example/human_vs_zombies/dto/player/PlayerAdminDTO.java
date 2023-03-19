package com.example.human_vs_zombies.dto.player;

import com.example.human_vs_zombies.entities.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/* This DTO created only for admins to get players details */
@Getter
@Setter
public class PlayerAdminDTO {
    private int player_id;
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
