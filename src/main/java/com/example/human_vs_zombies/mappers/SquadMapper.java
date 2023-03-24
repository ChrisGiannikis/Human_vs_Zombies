package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.squad.SquadDTO;
import com.example.human_vs_zombies.dto.squad.SquadPostDTO;
import com.example.human_vs_zombies.entities.Squad;
import com.example.human_vs_zombies.services.game.GameService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class SquadMapper {

    @Autowired
    protected GameService gameService;

    @Mapping(target = "game", source = "game.game_id")
    public abstract SquadDTO squadToSquadDto(Squad squad);

    @Mapping(target = "squad_id", ignore = true)
    @Mapping(target = "human", ignore = true)
    @Mapping(target = "game", ignore = true)
    @Mapping(target = "chat", ignore = true)
    @Mapping(target = "squadMembers", ignore = true)
    public abstract Squad squadPostDTOToSquad(SquadPostDTO squadPostDTO);

//    @Mapping(target = "squad_id", ignore = true)
//    @Mapping(target = "human", ignore = true)
//    @Mapping(target = "game", ignore = true)
//    @Mapping(target = "chat", ignore = true)
//    @Mapping(target = "squadMembers", ignore = true)
//    public abstract Squad squadPutDTOToSquad(SquadPutDTO squadPutDTO);

    public abstract Collection<SquadDTO> squadToSquadDto(Collection<Squad> squads);
}
