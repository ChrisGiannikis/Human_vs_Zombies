package com.example.human_vs_zombies.services.game;

import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.entities.Mission;
import com.example.human_vs_zombies.exceptions.GameNotFoundException;
import com.example.human_vs_zombies.exceptions.MissionNotFoundException;
import com.example.human_vs_zombies.repositories.GameRepository;
import com.example.human_vs_zombies.repositories.MissionRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
@Service
public class GameServiceImpl implements GameService{

    private final GameRepository gameRepository;
    private final MissionRepository missionRepository;

    public GameServiceImpl(GameRepository gameRepository, MissionRepository missionRepository){
        this.gameRepository = gameRepository;
        this.missionRepository = missionRepository;
    }

    @Override
    public Game findById(Integer id){
        return gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
    }

    @Override
    public Collection<Game> findAll() {
        return gameRepository.findAll();
    }

    @Override
    public Game add(Game entity) {return gameRepository.save(entity);}

    @Override
    public Game update(Game entity) {
        return gameRepository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {

        if(gameRepository.existsById(id)) {

            gameRepository.deleteById(id);
        }

    }

    @Override
    public Mission findMissionById(int game_id, int mission_id) {
        Game game = gameRepository.findById(game_id).orElseThrow(() -> new GameNotFoundException(game_id));
        Mission mission = missionRepository.findById(mission_id).orElseThrow(() -> new MissionNotFoundException(mission_id));
        Collection<Mission> missions = game.getMissions();

        if(missions.contains(mission)){
            return mission;
        }

        return null;
    }

    @Override
    public void addMission(int game_id, Mission mission) {
        Game game = gameRepository.findById(game_id).orElseThrow(() -> new GameNotFoundException(game_id));
        Set<Mission> missions = game.getMissions();
        missions.add(mission);
        game.setMissions(missions);

        missionRepository.save(mission);
    }

    @Override
    public void updateMission(int gameId, int missionId, Mission updatedMission) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new MissionNotFoundException(missionId));
        Set<Mission> missions = game.getMissions();
        missions.remove(mission);
        missions.add(updatedMission);
        game.setMissions(missions);
        gameRepository.save(game);

        missionRepository.save(updatedMission);
    }

    @Override
    public void deleteMissionById(int gameId, int missionId) {

        missionRepository.deleteById(missionId);
    }
}
