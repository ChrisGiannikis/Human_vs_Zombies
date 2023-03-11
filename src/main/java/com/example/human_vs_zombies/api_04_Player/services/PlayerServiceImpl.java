package com.example.human_vs_zombies.api_04_Player.services;

import com.example.human_vs_zombies.api_04_Player.repositories.PlayerRepository;
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
    public Player findById(Integer integer) {
        return null;
    }

    @Override
    public Collection<Player> findAll() {
        return null;
    }

    @Override
    public Player add(Player entity) {
        return null;
    }

    @Override
    public Player update(Player entity) {
        return null;
    }

    @Override
    public void deleteById(Integer integer) {

    }
}
