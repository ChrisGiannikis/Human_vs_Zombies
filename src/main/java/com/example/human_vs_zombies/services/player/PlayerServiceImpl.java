package com.example.human_vs_zombies.services.player;

import com.example.human_vs_zombies.exceptions.PlayerNotFoundException;
import com.example.human_vs_zombies.repositories.PlayerRepository;
import com.example.human_vs_zombies.entities.Player;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
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
        Player playerToUpdate = playerRepository.findById(id).get();
        //Setting the attributes of the requested player as the attributes of the given player
        playerToUpdate.set_human(player.is_human()); //passing the is_human attribute
        playerToUpdate.set_patient_zero(player.is_patient_zero());  //passing the is_human attribute
        player.setGame(player.getGame()); //passing the game attribute
        player.setDeath(player.getDeath()); // passing the death attribute
        return playerRepository.save(playerToUpdate); //saving the updated player object
    }

    @Override
    public void deleteById(Integer id) {
        //1) delete any foreign keys to be able to delete this Player

        //2) delete this Player
        playerRepository.deleteById(id);
    }
}
