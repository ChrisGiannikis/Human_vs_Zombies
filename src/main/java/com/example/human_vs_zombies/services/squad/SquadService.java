package com.example.human_vs_zombies.services.squad;

import com.example.human_vs_zombies.entities.Squad;
import com.example.human_vs_zombies.entities.SquadMember;
import com.example.human_vs_zombies.services.CrudService;

public interface SquadService extends CrudService<Squad, Integer> {
    void increaseDeceasedCount(SquadMember squadMember);

    int getDeceasedSquadMembers(int id);
}
