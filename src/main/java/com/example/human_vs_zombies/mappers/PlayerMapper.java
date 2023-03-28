package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.player.PlayerDTO;
import com.example.human_vs_zombies.dto.player.PlayerNotAdminDTO;
import com.example.human_vs_zombies.dto.player.PlayerPostDTO;
import com.example.human_vs_zombies.entities.*;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.squadMember.SquadMemberService;
import com.example.human_vs_zombies.services.user.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class PlayerMapper {
    @Autowired
    protected UserService userService;
    @Autowired
    protected GameService gameService;
    @Autowired
    protected SquadMemberService squadMemberService;

    @Mapping(target = "user", source = "user.user_id")
    @Mapping(target = "game", source = "game.game_id")
    //@Mapping(target = "death", source = "")
    //@Mapping(target = "kills", source = "kills", qualifiedByName = "killsToKillsId")
    //@Mapping(target = "squadMember", source = "squadMember.squad_member_id")
    //@Mapping(target = "chat", source = "chat", qualifiedByName = "chatToMessageId")
    @Mapping(target = "full_name", source = "user.user_id", qualifiedByName = "UserNamesToFullName")
    public abstract PlayerNotAdminDTO playerToPlayerAdminDTO(Player player);     //mapper for PlayerNotAdminDTO
    public abstract Collection<PlayerNotAdminDTO> playerToPlayerAdminDTO(Collection<Player> players);  //mapper for PlayerAdminDTO to handle a collection of Players

    @Mapping(target = "user", source = "user.user_id")
    @Mapping(target = "game", source = "game.game_id")
    @Mapping(target = "full_name", source = "user.user_id", qualifiedByName = "UserNamesToFullName")
    public abstract PlayerDTO playerToPlayerSimpleDTO(Player player);  //mapper for PlayerSimpleDTO
    public abstract Collection<PlayerDTO> playerToPlayerSimpleDTO(Collection<Player> player);  //mapper for PlayerSimpleDTO to handle a collection of Players

    @Mapping(target = "player_id", ignore = true)
    @Mapping(target = "death", ignore = true)
    @Mapping(target = "kills", ignore = true)
    @Mapping(target = "squadMember", ignore = true)
    @Mapping(target = "chat", ignore = true)
    @Mapping(target = "game", ignore = true)
    @Mapping(target = "biteCode", ignore = true)
    @Mapping(target = "user",source = "user", qualifiedByName = "UserIdToUser")
    public abstract Player playerPostDTOtoPlayer(PlayerPostDTO playerPostDTO); //mapper to convert playerPostDTO to player

//    @Mapping(target = "player_id", source = "player_id", qualifiedByName = "")
//    @Mapping(target = "death", source = "")
//    @Mapping(target = "player_id", ignore = true)
//    @Mapping(target = "death", ignore = true)
//    @Mapping(target = "kills", ignore = true)
//    @Mapping(target = "squadMember", ignore = true)
//    @Mapping(target = "chat", ignore = true)
//    @Mapping(target = "user", ignore = true)
//    @Mapping(target = "game", ignore = true)
////    @Mapping(target = "squadMember",source = "squadMember", qualifiedByName = "SquadMemberIdToSquadMember")
//    public abstract Player playerPutDTOtoPlayer(PlayerPutDTO playerPutDTO); //mapper to convert playerPutDTO to player

    @Named("UserIdToUser")
    AppUser mapIdToUser(int id){ return userService.findById(id); }

    @Named("UserNamesToFullName")
    String mapNames(int id){
        AppUser user = userService.findById(id);
        return user.getFirst_name() + " " + user.getLast_name();
    }
}
