package com.example.human_vs_zombies.services.kill;

import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.exceptions.KillNotFoundException;
import com.example.human_vs_zombies.repositories.KillRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class KillServiceImpl implements KillService {

    private final KillRepository killRepository;

    public KillServiceImpl(KillRepository killRepository) {
        this.killRepository = killRepository;
    }

    @Override
    public Kill findById(Integer id) {
        return killRepository.findById(id).orElseThrow(() -> new KillNotFoundException(id));
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
    public Kill update(Kill updatedKill) {
        Kill kill = this.findById(updatedKill.getKill_id());
        updatedKill.setKiller(kill.getKiller());
        updatedKill.setVictim(kill.getVictim());
////        Player victim = kill.getVictim();
////        victim.setDeath(kill);
////
////        Set<Kill> kills = kill.getKiller().getKills();
////        kills.add(kill);
//        kill.getKiller().setKills(kills);

        return killRepository.save(updatedKill);
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
    public Collection<Kill> findAllKillsByGameId(int gameId) {
        return killRepository.findAllKillsByGameId(gameId);
    }

    @Override
    public Kill findKillByKillIdAndGameId(int gameId, int killId) {
        return killRepository.findKillByKillIdAndGameId(gameId, killId);
    }


    @Override
    public void deleteById(Integer id) {

        Kill kill = this.findById(id);
        kill.getVictim().setDeath(null);
        kill.getKiller().getKills().remove(kill);
        killRepository.deleteById(id);

    }
}
