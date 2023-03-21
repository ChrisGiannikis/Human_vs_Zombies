package com.example.human_vs_zombies.services.game;

import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.entities.Mission;
import com.example.human_vs_zombies.services.CrudService;

import java.util.Collection;

public interface GameService extends CrudService<Game, Integer> {

    Mission findMissionById(int game_id, int mission_id);

    void addMission(int game_id, Mission mission);

    void updateMission(int gameId, int missionId, Mission updatedMission);

    void deleteMissionById(int missionId);

    Collection<Kill> findAllKills(int gameId);

    Kill findKillById(int game_id, int kill_id);

    void deleteKillById(int killId);
}
