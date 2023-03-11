package com.example.human_vs_zombies.api_04_Player.mappers;

import com.example.human_vs_zombies.api_04_Player.dto.PlayerAdminDTO;
import com.example.human_vs_zombies.api_04_Player.dto.PlayerSimpleDTO;
import com.example.human_vs_zombies.entities.Player;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    PlayerAdminDTO playerToPlayerAdminDTO(Player player);     //mapper for PlayerAdminDTO
    Collection<PlayerAdminDTO> playerToPlayerAdminDTO(Collection<Player> player);  //mapper for PlayerAdminDTO to handle a collection of Players

    PlayerSimpleDTO playerToPlayerSimpleDTO(Player player);  //mapper for PlayerSimpleDTO
    Collection<PlayerSimpleDTO> playerToPlayerSimpleDTO(Collection<Player> player);  //mapper for PlayerSimpleDTO to handle a collection of Players
}
