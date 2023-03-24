package com.example.human_vs_zombies.repositories;

import com.example.human_vs_zombies.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    @Query("select s from Player s where s.biteCode like ?1")
    Player findByBiteCode(String biteCode);
}
