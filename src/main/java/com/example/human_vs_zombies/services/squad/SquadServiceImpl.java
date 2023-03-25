package com.example.human_vs_zombies.services.squad;

import com.example.human_vs_zombies.entities.Squad;
import com.example.human_vs_zombies.entities.SquadMember;
import com.example.human_vs_zombies.exceptions.SquadNotFoundException;
import com.example.human_vs_zombies.repositories.SquadRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;

import static java.util.Objects.isNull;

@Service
public class SquadServiceImpl implements SquadService{

    private final SquadRepository squadRepository;

    private final HashMap<Integer, Integer> deceasedMap = new HashMap<>();

    public SquadServiceImpl(SquadRepository squadRepository) {
        this.squadRepository = squadRepository;
    }

    @Override
    public Squad findById(Integer id) {
        return squadRepository.findById(id).orElseThrow(() -> new SquadNotFoundException(id));
    }

    @Override
    public Collection<Squad> findAll() {
        return squadRepository.findAll();
    }

    @Override
    public Squad add(Squad squad)
    {
        return squadRepository.save(squad);
    }

    @Override
    public Squad update(Squad entity) {
        return squadRepository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        if(squadRepository.existsById(id)){

            Squad squad = this.findById(id);
            squad.getGame().getSquads().remove(squad);

            squadRepository.deleteById(id);
        }
    }

    @Override
    public void increaseDeceasedCount(SquadMember squadMember) {
        Squad squad = squadMember.getSquad();
        if(deceasedMap.containsKey(squad.getSquad_id())){
            deceasedMap.put(squad.getSquad_id(), deceasedMap.get(squad.getSquad_id()) + 1);
        }else{
            deceasedMap.put(squad.getSquad_id(), 1);
        }
    }

    @Override
    public int getDeceasedSquadMembers(int id){
        if(!isNull(deceasedMap.get(id))) {
            return deceasedMap.get(id);
        }

        return 0;
    }
}
