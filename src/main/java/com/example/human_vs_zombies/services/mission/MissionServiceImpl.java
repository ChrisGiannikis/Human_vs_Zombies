package com.example.human_vs_zombies.services.mission;

import com.example.human_vs_zombies.entities.Mission;
import com.example.human_vs_zombies.exceptions.MissionNotFoundException;
import com.example.human_vs_zombies.repositories.MissionRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;

    public MissionServiceImpl(MissionRepository missionRepository) { this.missionRepository = missionRepository; }

    @Override
    public Mission findById(Integer id) { return missionRepository.findById(id).orElseThrow(() -> new MissionNotFoundException(id)); }

    @Override
    public Collection<Mission> findAll() { return missionRepository.findAll(); }

    @Override
    public Mission add(Mission entity) { return missionRepository.save(entity); }

    @Override
    public Mission update(Mission entity) { return missionRepository.save(entity); }

    @Override
    public void deleteById(Integer id) {
        //1) delete any foreign keys to be able to delete this mission

        //2) delete this mission
        missionRepository.deleteById(id);
    }
}
