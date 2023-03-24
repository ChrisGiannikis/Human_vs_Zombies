package com.example.human_vs_zombies.services.kill;

import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.services.CrudService;

public interface KillService extends CrudService<Kill,Integer> {
    void updateKillById(Kill kill,int id);
}
