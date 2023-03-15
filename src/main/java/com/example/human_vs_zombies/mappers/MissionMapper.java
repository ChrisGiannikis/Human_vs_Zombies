package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.mission.MissionDTO;
import com.example.human_vs_zombies.dto.mission.MissionPostDTO;
import com.example.human_vs_zombies.entities.Mission;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface MissionMapper {

    MissionDTO missionToMissionDTO(Mission mission);

    Collection<MissionDTO> missionToMissionDTO(Collection<Mission> missions);

    Mission missionPostDTOToMission(MissionPostDTO missionPostDTO);
}
