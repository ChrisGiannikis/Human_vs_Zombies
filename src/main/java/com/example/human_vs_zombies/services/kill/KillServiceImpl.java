package com.example.human_vs_zombies.services.kill;

import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.exceptions.KillNotFoundException;
import com.example.human_vs_zombies.repositories.KillRepository;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
public class KillServiceImpl implements KillService {

    @Autowired
    private final KillRepository killRepository;

    @Autowired
    private PlayerService playerService;





    public KillServiceImpl(KillRepository killRepository) {
        this.killRepository = killRepository;
    }

    @Override
    public Kill findById(Integer id) {
        return killRepository.findById(id).orElseThrow(() -> new KillNotFoundException(id));
    }
    @Override
    public Collection<Kill> findKillsByGameId(Integer gameId) {
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
        this.findById(kill.getKill_id());
        return killRepository.save(kill);
    }

    @Override
    public void updateKillById(Kill kill,int id) {
        Kill killToUpdate = killRepository.findById(id).orElseThrow(() -> new KillNotFoundException(id));
        killToUpdate.setTime_of_death(kill.getTime_of_death());
        killToUpdate.setLat(kill.getLat());
        killToUpdate.setLng(kill.getLng());
        killRepository.save(killToUpdate);
    }

    @Override
    public void deleteById(Integer id) {

        if(killRepository.existsById(id)){
            killRepository.deleteById(id);
        }

    }

    @Override
    public Kill findByGameIdAndKillId(int gameId, int killId) {
        return this.findKillsByGameId(gameId).stream().filter(kill -> kill.getKill_id() == killId).findFirst().orElseThrow(()->new KillNotFoundException(killId));
    }

}
