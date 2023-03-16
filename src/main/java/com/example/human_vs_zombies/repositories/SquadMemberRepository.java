package com.example.human_vs_zombies.repositories;

import com.example.human_vs_zombies.entities.SquadMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SquadMemberRepository extends JpaRepository<SquadMember, Integer> {
}
