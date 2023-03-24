package com.example.human_vs_zombies.services.mission;

import com.example.human_vs_zombies.entities.Mission;
import com.example.human_vs_zombies.exceptions.MissionNotFoundException;
import com.example.human_vs_zombies.repositories.MissionRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;

    public MissionServiceImpl(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    @Override
    public Mission findById(Integer id) { return missionRepository.findById(id).orElseThrow(() -> new MissionNotFoundException(id)); }

    @Override
    public Collection<Mission> findAll() { return missionRepository.findAll(); }

    @Override
    public Mission add(Mission mission) {
        Set<Mission> missions = mission.getGame().getMissions();
        missions.add(mission);
        mission.getGame().setMissions(missions);
        return missionRepository.save(mission);
    }

    @Override
    public Mission update(Mission mission) { return missionRepository.save(mission); }

    @Override
    public void deleteById(Integer id) {
        //1) check if mission exists;
        Mission mission = this.findById(id);
        mission.getGame().getMissions().remove(mission);
        //3) delete this mission
        missionRepository.deleteById(id);
    }
}
