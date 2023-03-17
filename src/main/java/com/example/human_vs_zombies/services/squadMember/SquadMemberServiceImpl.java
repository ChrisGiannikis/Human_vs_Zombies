package com.example.human_vs_zombies.services.squadMember;

import com.example.human_vs_zombies.entities.SquadMember;
import com.example.human_vs_zombies.exceptions.SquadMemberNotFoundException;
import com.example.human_vs_zombies.repositories.SquadMemberRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SquadMemberServiceImpl implements SquadMemberService{

    private final SquadMemberRepository squadMemberRepository;

    public SquadMemberServiceImpl(SquadMemberRepository squadMemberRepository) {
        this.squadMemberRepository = squadMemberRepository;
    }

    @Override
    public SquadMember findById(Integer id) {
        return squadMemberRepository.findById(id).orElseThrow(() -> new SquadMemberNotFoundException(id));
    }

    @Override
    public Collection<SquadMember> findAll() {
        return squadMemberRepository.findAll();
    }

    @Override
    public SquadMember add(SquadMember entity) {
        return squadMemberRepository.save(entity);
    }

    @Override
    public SquadMember update(SquadMember entity) {
        return squadMemberRepository.save(entity);
    }

    @Override
    public void deleteById(Integer integer) {
        //Must create SquadCheckIn
    }
}
