package com.example.human_vs_zombies.dto.squadMember;

import com.example.human_vs_zombies.enums.Rank;
import lombok.Data;

@Data
public class SquadMemberDTO {

    private int squad_member_id;

    private Rank rank;

    private int squad;

    private int player;
}
