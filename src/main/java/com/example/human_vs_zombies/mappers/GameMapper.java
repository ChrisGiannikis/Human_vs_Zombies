package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.game.GameDTO;
import com.example.human_vs_zombies.dto.game.GamePostDTO;
import com.example.human_vs_zombies.dto.game.GamePutDTO;
import com.example.human_vs_zombies.entities.AppUser;
import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.services.game.GameService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class GameMapper {

    @Autowired
    protected GameService gameService;

    @Mapping(target = "members", source = "game_id", qualifiedByName = "membersToMembersId")
    public abstract GameDTO gameToGameDto(Game game);

    @Mapping(target = "game_id", ignore = true)
    @Mapping(target = "players", ignore = true)
    @Mapping(target = "missions", ignore = true)
    @Mapping(target = "squads", ignore = true)
    @Mapping(target = "state", ignore = true)
    public abstract Game gamePostDtoToGame(GamePostDTO gamePostDTO);

    @Mapping(target = "game_id", ignore = true)
    @Mapping(target = "players", ignore = true)
    @Mapping(target = "missions", ignore = true)
    @Mapping(target = "squads", ignore = true)
    @Mapping(target = "state", ignore = true)
    public abstract Game gamePutDtoToGame(GamePutDTO gamePutDTO);


    public abstract Collection<GameDTO> gameToGameDto(Collection<Game> games);

    @Named("membersToMembersId")
    Integer mapMembers(int id){
        Game game = gameService.findById(id);
        return game.getPlayers().size();
    }
}
