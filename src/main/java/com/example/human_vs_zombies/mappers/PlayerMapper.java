package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.PlayerAdminDTO;
import com.example.human_vs_zombies.dto.PlayerSimpleDTO;
import com.example.human_vs_zombies.entities.Player;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    PlayerAdminDTO playerToPlayerAdminDTO(Player player);     //mapper for PlayerAdminDTO
    Collection<PlayerAdminDTO> playerToPlayerAdminDTO(Collection<Player> player);  //mapper for PlayerAdminDTO to handle a collection of Players
    Player playerAdminDTOtoPlayer(PlayerAdminDTO playerAdminDTO); //mapper to convert playerAdminDTO to player

    PlayerSimpleDTO playerToPlayerSimpleDTO(Player player);  //mapper for PlayerSimpleDTO
    Collection<PlayerSimpleDTO> playerToPlayerSimpleDTO(Collection<Player> player);  //mapper for PlayerSimpleDTO to handle a collection of Players
}
