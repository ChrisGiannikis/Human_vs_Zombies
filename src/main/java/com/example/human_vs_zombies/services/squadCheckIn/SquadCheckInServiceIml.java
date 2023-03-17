package com.example.human_vs_zombies.services.squadCheckIn;

import com.example.human_vs_zombies.entities.SquadCheckIn;
import com.example.human_vs_zombies.repositories.SquadCheckInRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SquadCheckInServiceIml implements SquadCheckInService {
    private final SquadCheckInRepository squadCheckInRepository;

    public SquadCheckInServiceIml(SquadCheckInRepository squadCheckInRepository) {
        this.squadCheckInRepository = squadCheckInRepository;
    }

    @Override
    public SquadCheckIn findById(Integer integer) { return null; }

    @Override
    public Collection<SquadCheckIn> findAll() { return squadCheckInRepository.findAll(); }

    @Override
    public SquadCheckIn add(SquadCheckIn entity) { return squadCheckInRepository.save(entity); }

    @Override
    public SquadCheckIn update(SquadCheckIn entity) { return null; }

    @Override
    public void deleteById(Integer integer) {
        //check if exists
        if(squadCheckInRepository.existsById(integer)){
            squadCheckInRepository.deleteById(integer);
        }

    }
}
