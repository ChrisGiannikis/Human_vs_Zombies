package com.example.human_vs_zombies.dto;

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
    private AppUser user;
    private Game game;
    private Kill death;
    private Set<Kill> kills; //maybe Set<Integer> to keep only a counter for kills
    private SquadMember squadMember;
    private Set<Chat> chat;
}