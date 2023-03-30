package com.example.human_vs_zombies.dto.squadMember;

import com.example.human_vs_zombies.enums.Rank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SquadMemberPostDTO {
    private Rank rank;

    private int squad;

    private int player;
}
