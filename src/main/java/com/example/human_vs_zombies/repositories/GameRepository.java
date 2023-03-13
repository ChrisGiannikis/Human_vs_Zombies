package com.example.human_vs_zombies.repositories;

import com.example.human_vs_zombies.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {


}
