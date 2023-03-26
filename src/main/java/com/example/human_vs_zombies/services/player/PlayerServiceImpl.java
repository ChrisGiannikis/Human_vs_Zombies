package com.example.human_vs_zombies.services.player;

import com.example.human_vs_zombies.entities.SquadMember;
import com.example.human_vs_zombies.exceptions.PlayerNotFoundException;
import com.example.human_vs_zombies.repositories.PlayerRepository;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.repositories.SquadMemberRepository;
import com.example.human_vs_zombies.services.squad.SquadService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.util.Objects.isNull;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final SquadMemberRepository squadMemberRepository;
    private final SquadService squadService;

    public PlayerServiceImpl(PlayerRepository playerRepository, SquadMemberRepository squadMemberRepository, SquadService squadService) {
        this.playerRepository = playerRepository;
        this.squadMemberRepository = squadMemberRepository;
        this.squadService = squadService;
    }

    @Override
    public Player findById(Integer id) { return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id)); }

    @Override
    public Collection<Player> findAll() { return playerRepository.findAll(); }

    @Override
    public Player add(Player player) {
        player.getGame().getPlayers().add(player);
        return playerRepository.save(player);
    }

    @Override
    public Player update(Player entity) { return playerRepository.save(entity); }

    @Override
    public Player findByBiteCode(String biteCode) {
        return playerRepository.findByBiteCode(biteCode);
    }

    @Transactional
    @Override
    public void turnHumanIntoZombie(int playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        player.setHuman(false);

        SquadMember squadMember = player.getSquadMember();

        if(!isNull(squadMember)){
            squadMemberRepository.deleteById(squadMember.getSquad_member_id());
            squadService.increaseDeceasedCount(squadMember);
            player.setSquadMember(null);
        }
    }

    @Transactional
    @Override
    public void changeTeams(int playerId) {
        Player player = this.findById(playerId);
        player.setHuman(!player.isHuman());
        player.setPatient_zero(!player.isHuman());

        SquadMember squadMember = player.getSquadMember();

        if(!isNull(squadMember)){
            squadMemberRepository.deleteById(squadMember.getSquad_member_id());
            player.setSquadMember(null);
        }

        playerRepository.save(player);
    }

    @Override
    public void deleteById(Integer id) {

        Player player = this.findById(id);
        player.getGame().getPlayers().remove(player);
        player.getUser().setPlayer(null);

        playerRepository.deleteById(id);
    }

//    @Override
//    public void deleteById(Integer id) {
//        //1)check if player exists
//        this.findById(id);
//        //2) delete any foreign keys to be able to delete this Player
//        Collection<Kill> kills = killService.findAll();     // gets all the kills
//        for (Kill k: kills) {                               //for every kill
//            if ( k.getVictim().getPlayer_id() == id )//if victim of this kill is the current user
//                killService.deleteById(k.getKill_id());     //delete this kill
//            if ( k.getKiller().getPlayer_id() == id )//if killer of this kill is the current user
//                killService.deleteById(k.getKill_id());     //delete this kill
//        }
//        //3) delete this Player
//        playerRepository.deleteById(id);
//    }
}
