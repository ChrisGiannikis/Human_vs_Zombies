package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.MissionDTO;
import com.example.human_vs_zombies.entities.Mission;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface MissionMapper {

    MissionDTO missonToMissionDTO(Mission mission);

    Collection<MissionDTO> missonToMissionDTO(Collection<Mission> missions);
}
