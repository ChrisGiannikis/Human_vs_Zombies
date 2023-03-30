package com.example.human_vs_zombies.services.squadMember;

import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.entities.SquadMember;
import com.example.human_vs_zombies.services.CrudService;

public interface SquadMemberService extends CrudService<SquadMember, Integer> {
    SquadMember addSquadLeader(Player player, int squad_id);
}
