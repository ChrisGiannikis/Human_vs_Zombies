package com.example.human_vs_zombies.services.player;

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
    public Player findById(Integer id) { return playerRepository.findById(id).get(); }

    @Override
    public Collection<Player> findAll() { return playerRepository.findAll(); }

    @Override
    public Player add(Player entity) { return playerRepository.save(entity); }

    @Override
    public Player update(Player entity) { return playerRepository.save(entity); }

    @Override
    public void deleteById(Integer id) {
        //1) delete any foreign keys to be able to delete this Player

        //2) delete this Player
        playerRepository.deleteById(id);
    }
}
