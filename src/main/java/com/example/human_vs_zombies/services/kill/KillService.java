package com.example.human_vs_zombies.services.kill;

import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.services.CrudService;

import java.util.Collection;

public interface KillService extends CrudService<Kill,Integer> {
    void updateKillById(Kill kill,int id);

    Collection<Kill> findAllKillsByGameId(int gameId);

    Kill findKillByKillIdAndGameId(int gameId, int killId);
}
