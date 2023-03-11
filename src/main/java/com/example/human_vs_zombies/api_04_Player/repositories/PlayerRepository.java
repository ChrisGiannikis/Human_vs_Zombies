package com.example.human_vs_zombies.api_04_Player.repositories;

import com.example.human_vs_zombies.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
}
