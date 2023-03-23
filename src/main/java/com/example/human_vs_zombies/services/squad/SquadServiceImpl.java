package com.example.human_vs_zombies.services.squad;

import com.example.human_vs_zombies.entities.Squad;
import com.example.human_vs_zombies.exceptions.SquadNotFoundException;
import com.example.human_vs_zombies.repositories.SquadRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SquadServiceImpl implements SquadService{

    private final SquadRepository squadRepository;

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
}
