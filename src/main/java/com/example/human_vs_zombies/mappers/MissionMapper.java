package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.mission.MissionDTO;
import com.example.human_vs_zombies.dto.mission.MissionPostDTO;
import com.example.human_vs_zombies.dto.mission.MissionPutDTO;
import com.example.human_vs_zombies.entities.Mission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface MissionMapper {

    @Mapping(target = "game", source = "game.game_id")
    MissionDTO missionToMissionDTO(Mission mission);

    Collection<MissionDTO> missionToMissionDTO(Collection<Mission> missions);

    Mission missionPostDTOToMission(MissionPostDTO missionPostDTO);
    Mission missionPutDTOToMission(MissionPutDTO missionPutDTO);
}
