package com.example.human_vs_zombies.repositories;

import com.example.human_vs_zombies.entities.Kill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface KillRepository extends JpaRepository<Kill,Integer> {

    @Query("select k from Kill k join Player p on k.victim.player_id = p.player_id join Game g on p.game.game_id = g.game_id where g.game_id = ?1")
    Collection<Kill> findAllKillsByGameId(int gameId);

    @Query("select k from Kill k join Player p on k.victim.player_id = p.player_id join Game g on p.game.game_id = g.game_id where g.game_id = ?1 and k.kill_id = ?2")
    Kill findKillByKillIdAndGameId(int gameId, int killId);
}
