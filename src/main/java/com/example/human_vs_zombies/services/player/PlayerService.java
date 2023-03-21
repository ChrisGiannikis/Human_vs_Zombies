package com.example.human_vs_zombies.services.player;

import com.example.human_vs_zombies.services.CrudService;
import com.example.human_vs_zombies.entities.Player;

public interface PlayerService extends CrudService<Player, Integer> {
    public Player updatePlayerById(Player player, int id);

    Player findByBiteCode(String biteCode);

    void turnHumanIntoZombie(int playerId);
}
