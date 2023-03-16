package com.example.human_vs_zombies.repositories;

import com.example.human_vs_zombies.entities.Squad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SquadRepository extends JpaRepository<Squad, Integer> {
}
