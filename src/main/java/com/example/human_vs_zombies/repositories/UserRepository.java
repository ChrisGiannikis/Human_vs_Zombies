package com.example.human_vs_zombies.repositories;

import com.example.human_vs_zombies.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<AppUser, String> {

    @Query("select u from AppUser u where u.user_id like ?1")
    AppUser findByIdStr(String id);
}
