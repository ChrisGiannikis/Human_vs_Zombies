package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.player.PlayerAdminDTO;
import com.example.human_vs_zombies.dto.player.PlayerPostDTO;
import com.example.human_vs_zombies.dto.player.PlayerPutDTO;
import com.example.human_vs_zombies.dto.player.PlayerSimpleDTO;
import com.example.human_vs_zombies.entities.Player;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    PlayerAdminDTO playerToPlayerAdminDTO(Player player);     //mapper for PlayerAdminDTO
    Collection<PlayerAdminDTO> playerToPlayerAdminDTO(Collection<Player> players);  //mapper for PlayerAdminDTO to handle a collection of Players
    Player playerPostDTOtoPlayer(PlayerPostDTO playerPostDTO); //mapper to convert playerPostDTO to player
    Player playerPutDTOtoPlayer(PlayerPutDTO playerPutDTO); //mapper to convert playerPutDTO to player

    PlayerSimpleDTO playerToPlayerSimpleDTO(Player player);  //mapper for PlayerSimpleDTO
    Collection<PlayerSimpleDTO> playerToPlayerSimpleDTO(Collection<Player> player);  //mapper for PlayerSimpleDTO to handle a collection of Players
}
