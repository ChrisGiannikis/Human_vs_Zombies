package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.GameDTO;
import com.example.human_vs_zombies.dto.kill.KillDTO;
import com.example.human_vs_zombies.dto.kill.KillPostDTO;
import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.player.PlayerService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface KillMapper {



     KillDTO killToKillDTO(Kill kill);
    Collection<Kill> killsToKillsDTO(Collection<Kill> kills);
     Kill killDTOToKill(KillDTO killDTO);
    KillPostDTO killToKillPostDTO(Kill kill);
     Kill KillPostDTOToKill(KillPostDTO killPostDTO);

}
