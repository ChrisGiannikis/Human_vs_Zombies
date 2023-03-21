package com.example.human_vs_zombies.dto.squadMember;

import com.example.human_vs_zombies.enums.Rank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SquadMemberPutDTO {
    private int squad_member_id;

    private Rank rank;

    private int squad;

    private int player;
}
