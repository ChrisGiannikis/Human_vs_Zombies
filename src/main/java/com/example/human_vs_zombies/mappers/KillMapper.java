package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.kill.KillDTO;
import com.example.human_vs_zombies.dto.kill.KillPostDTO;
import com.example.human_vs_zombies.entities.Kill;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface KillMapper {



     KillDTO killToKillDTO(Kill kill);
    Collection<Kill> killsToKillsDTO(Collection<Kill> kills);
     Kill killDTOToKill(KillDTO killDTO);
    Kill killToKillPostDTO(Kill kill);
     Kill KillPostDTOToKill(KillPostDTO killPostDTO);

}
