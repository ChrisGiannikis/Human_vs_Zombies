package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.kill.KillDTO;
import com.example.human_vs_zombies.dto.kill.KillPostDTO;
import com.example.human_vs_zombies.dto.kill.KillPutDTO;
import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.services.player.PlayerService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class KillMapper {

    @Autowired
    protected PlayerService playerService;

//    @Mapping(target = "game", source = "game.game_id")
    @Mapping(target = "killer", source = "killer.player_id")
    @Mapping(target = "victim", source = "victim.player_id")
    public abstract KillDTO killToKillDTO(Kill kill);

    public abstract Collection<KillDTO> killsToKillsDTO(Collection<Kill> kills);


    //KillDTO -> Kill
//    @Mapping(target = "game", source = "game", qualifiedByName = "gameIdToGame")
    @Mapping(target = "victim", source = "victim", qualifiedByName = "playerIdToPlayer")
    @Mapping(target = "killer", source = "killer", qualifiedByName = "playerIdToPlayer")
    public abstract Kill killDTOToKill(KillDTO killDTO);

//    @Mapping(target = "game", source = "game.game_id")
//    public abstract KillPostDTO killToKillPostDTO(Kill kill);

    @Mapping(target = "kill_id", ignore = true)
    @Mapping(target = "victim", source = "victim", qualifiedByName = "playerIdToPlayer")
    @Mapping(target = "killer", source = "killer", qualifiedByName = "playerIdToPlayer")
    public abstract Kill killPostDTOToKill(KillPostDTO killPostDTO);

    @Mapping(target = "kill_id", ignore = true)
    @Mapping(target = "victim", ignore = true)
    @Mapping(target = "killer", ignore = true)
    public abstract Kill killPutDTOToKill(KillPutDTO killPutDTO);

    @Named("playerIdToPlayer")
    Player mapIdToPlayer(Integer id) {
        if (id != null) {
            return playerService.findById(id);
        }
        return null;
    }

//    @Named("gameIdToGame")
//    Game mapIdToGame(Integer gameId) {
//        if (gameId != null) {
//            return gameService.findById(gameId);
//        }
//        return null;
//    }
}