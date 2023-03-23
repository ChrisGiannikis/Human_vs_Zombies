package com.example.human_vs_zombies.services.squadMember;

import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.entities.SquadMember;
import com.example.human_vs_zombies.enums.Rank;
import com.example.human_vs_zombies.exceptions.SquadMemberNotFoundException;
import com.example.human_vs_zombies.repositories.SquadCheckInRepository;
import com.example.human_vs_zombies.repositories.SquadMemberRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SquadMemberServiceImpl implements SquadMemberService{

    private final SquadMemberRepository squadMemberRepository;

    public SquadMemberServiceImpl(SquadMemberRepository squadMemberRepository, SquadCheckInRepository squadCheckInRepository) {
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
    public SquadMember add(SquadMember squadMember) {
        return squadMemberRepository.save(squadMember);
    }

    @Override
    public SquadMember update(SquadMember entity) {
        return squadMemberRepository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        if(squadMemberRepository.existsById(id)){
//            SquadMember squadMember = squadMemberRepository.findById(id).orElseThrow(RuntimeException::new);
//            Collection<SquadCheckIn> squadCheckIns = squadMember.getSquadCheckIns();
//            for(SquadCheckIn squadCheckIn: squadCheckIns) {
//                squadCheckInRepository.deleteById(squadCheckIn.getSquad_checkin_id());
//            }
            squadMemberRepository.deleteById(id);
        }
    }

    @Override
    public SquadMember addSquadLeader(Player player, int squad_id) {
        SquadMember squadMember = new SquadMember();
        squadMember.setPlayer(player);
        squadMember.setRank(Rank.LEADER);
        return squadMemberRepository.save(squadMember);
    }
}
