package com.example.human_vs_zombies.services.squadCheckIn;

import com.example.human_vs_zombies.entities.SquadCheckIn;
import com.example.human_vs_zombies.exceptions.SquadCheckInNotFoundException;
import com.example.human_vs_zombies.repositories.SquadCheckInRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class SquadCheckInServiceImpl implements SquadCheckInService {
    private final SquadCheckInRepository squadCheckInRepository;

    public SquadCheckInServiceImpl(SquadCheckInRepository squadCheckInRepository) {
        this.squadCheckInRepository = squadCheckInRepository;
    }

    @Override
    public SquadCheckIn findById(Integer integer) { return squadCheckInRepository.findById(integer).orElseThrow(() -> new SquadCheckInNotFoundException(integer)); }

    @Override
    public Collection<SquadCheckIn> findAll() { return squadCheckInRepository.findAll(); }

    @Override
    public SquadCheckIn add(SquadCheckIn squadCheckIn) {

        Set<SquadCheckIn> squadCheckIns = squadCheckIn.getSquadMember().getSquadCheckIns();
        squadCheckIns.add(squadCheckIn);
        squadCheckIn.getSquadMember().setSquadCheckIns(squadCheckIns);
        return squadCheckInRepository.save(squadCheckIn);
    }

    @Override
    public SquadCheckIn update(SquadCheckIn entity) { return null; }

    @Override
    public void deleteById(Integer integer) {
        //check if exists
        this.findById(integer);
        squadCheckInRepository.deleteById(integer);
    }
}
