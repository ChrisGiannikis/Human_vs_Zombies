package com.example.human_vs_zombies.services.player;

import com.example.human_vs_zombies.services.CrudService;
import com.example.human_vs_zombies.entities.Player;
import jakarta.transaction.Transactional;

public interface PlayerService extends CrudService<Player, Integer> {

    Player findByBiteCode(String biteCode);

    void turnHumanIntoZombie(int playerId);

    @Transactional
    void changeTeams(int playerId);
}
