package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.SquadDTO;
import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.entities.Squad;
import com.example.human_vs_zombies.services.game.GameService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class SquadMapper {

    @Autowired
    protected GameService gameService;

    @Mapping(target = "game", source = "game.game_id")
    public abstract SquadDTO squadToSquadDto(Squad squad);

    @Mapping(target = "game", source = "game", qualifiedByName = "gameIdToGame")
    @Mapping(target = "chat", ignore = true)
    @Mapping(target = "squadMembers", ignore = true)
    public abstract Squad squadDtoToSquad(SquadDTO squadDTO);

    @Named("gameIdToGame")
    Game mapIdToGame(int id) {return gameService.findById(id);}

    public abstract Collection<SquadDTO> squadToSquadDto(Collection<Squad> squads);
}
