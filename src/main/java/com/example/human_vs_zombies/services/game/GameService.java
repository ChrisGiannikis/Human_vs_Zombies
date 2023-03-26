package com.example.human_vs_zombies.services.game;

import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.services.CrudService;

public interface GameService extends CrudService<Game, Integer> {

    Player findPlayerById(int game_id, int player_id);

    void addPlayer(int game_id, Player player);

    void updatePlayer(int gameId, int playerId, Player player);

}
