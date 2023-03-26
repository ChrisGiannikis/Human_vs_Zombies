package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.mission.MissionDTO;
import com.example.human_vs_zombies.dto.mission.MissionPostDTO;
import com.example.human_vs_zombies.dto.mission.MissionPutDTO;
import com.example.human_vs_zombies.entities.Mission;
import com.example.human_vs_zombies.services.game.GameService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class MissionMapper {
    @Autowired
    protected GameService gameService;

    @Mapping(target = "game", source = "game.game_id")
    public abstract MissionDTO missionToMissionDTO(Mission mission);
    public abstract Collection<MissionDTO> missionToMissionDTO(Collection<Mission> missions);

    @Mapping(target = "game", ignore = true)
    @Mapping(target = "mission_id", ignore = true)
    public abstract Mission missionPostDTOToMission(MissionPostDTO missionPostDTO);

//    @Mapping(target = "game", source = "game", qualifiedByName = "MissionIdToMission")
    @Mapping(target = "mission_id", ignore = true)
    @Mapping(target = "game", ignore = true)
    public abstract Mission missionPutDTOToMission(MissionPutDTO missionPutDTO);

}
