package com.example.human_vs_zombies.services.kill;

import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.repositories.KillRepository;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
public class KillServiceImpl implements KillService {

    private final KillRepository killRepository;

    @Autowired
    protected PlayerService playerService;
    @Autowired
    protected GameService gameService;


    public KillServiceImpl(KillRepository killRepository) {
        this.killRepository = killRepository;
    }

    @Override
    public Kill findById(Integer id) {
        return killRepository.findById(id).get();
    }
    @Override
    public Collection<Kill> findKillsByGameId(Integer gameId) {
       // return gameService.findById(gameId).getKills();
       return playerService.findById(gameId).getKills();
    }


    @Override
    public Collection<Kill> findAll() {

        return killRepository.findAll();
    }

    @Override
    public Kill add(Kill kill) {

        return killRepository.save(kill);
    }

    @Override
    public Kill update(Kill kill) {
        return killRepository.save(kill);
    }

    @Override
    public Kill updateKillById(Kill kill,int id) {
        Kill killToUpdate = killRepository.findById(id).get();
        killToUpdate.setLat(kill.getLat());
        killToUpdate.setLng(kill.getLng());
        return killRepository.save(killToUpdate);
    }

    @Override
    public void deleteById(Integer id) {

        killRepository.deleteById(id);


    }
}
