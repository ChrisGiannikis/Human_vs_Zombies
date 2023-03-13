package com.example.human_vs_zombies.repositories;

import com.example.human_vs_zombies.entities.Kill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface KillRepository extends JpaRepository<Kill,Integer> {
}
