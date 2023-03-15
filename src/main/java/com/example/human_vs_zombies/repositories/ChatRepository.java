package com.example.human_vs_zombies.repositories;

import com.example.human_vs_zombies.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    
}
