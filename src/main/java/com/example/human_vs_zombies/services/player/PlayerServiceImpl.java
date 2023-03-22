package com.example.human_vs_zombies.services.player;

import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.entities.SquadMember;
import com.example.human_vs_zombies.exceptions.PlayerNotFoundException;
import com.example.human_vs_zombies.repositories.PlayerRepository;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.repositories.SquadMemberRepository;
import com.example.human_vs_zombies.services.kill.KillService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.util.Objects.isNull;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final SquadMemberRepository squadMemberRepository;
    private final KillService killService;

    public PlayerServiceImpl(PlayerRepository playerRepository, SquadMemberRepository squadMemberRepository, KillService killService) {
        this.playerRepository = playerRepository;
        this.squadMemberRepository = squadMemberRepository;
        this.killService = killService;
    }

    @Override
    public Player findById(Integer id) { return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id)); }

    @Override
    public Collection<Player> findAll() { return playerRepository.findAll(); }

    @Override
    public Player add(Player entity) { return playerRepository.save(entity); }

    @Override
    public Player update(Player entity) { return playerRepository.save(entity); }

    @Override
    public Player updatePlayerById(Player player, int id) {
        //Make a new object of Player and find the player with the id we want to update
        Player playerToUpdate = playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
        //Setting the attributes of the requested player as the attributes of the given player
        playerToUpdate.setHuman(player.isHuman()); //passing the is_human attribute
        playerToUpdate.setPatient_zero(player.isPatient_zero());  //passing the is_human attribute
        player.setGame(player.getGame()); //passing the game attribute
        player.setDeath(player.getDeath()); // passing the death attribute
        return playerRepository.save(playerToUpdate); //saving the updated player object
    }

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
            player.setSquadMember(null);
        }
    }

    @Override
    public void deleteById(Integer id) {
        //1)check if player exists
        this.findById(id);
        //2) delete any foreign keys to be able to delete this Player
        Collection<Kill> kills = killService.findAll();     // gets all the kills
        for (Kill k: kills) {                               //for every kill
            if ( k.getVictim().getPlayer_id() == id )//if victim of this kill is the current user
                killService.deleteById(k.getKill_id());     //delete this kill
            if ( k.getKiller().getPlayer_id() == id )//if killer of this kill is the current user
                killService.deleteById(k.getKill_id());     //delete this kill
        }
        //3) delete this Player
        playerRepository.deleteById(id);
    }
}
