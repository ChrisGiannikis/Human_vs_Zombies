package com.example.human_vs_zombies.repositories;

import com.example.human_vs_zombies.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Integer> {
}
