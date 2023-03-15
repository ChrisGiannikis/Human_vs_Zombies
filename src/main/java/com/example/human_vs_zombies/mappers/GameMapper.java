package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.GameDTO;
import com.example.human_vs_zombies.entities.Game;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface GameMapper {


    GameDTO gameToGameDto(Game game);

    @Mapping(target = "players", ignore = true)
    @Mapping(target = "missions", ignore = true)
    @Mapping(target = "squads", ignore = true)
    Game gameDtoToGame(GameDTO gameDTO);

    Collection<GameDTO> gameToGameDto(Collection<Game> games);
}
