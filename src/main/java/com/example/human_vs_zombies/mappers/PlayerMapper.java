package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.player.PlayerAdminDTO;
import com.example.human_vs_zombies.dto.player.PlayerPostDTO;
import com.example.human_vs_zombies.dto.player.PlayerPutDTO;
import com.example.human_vs_zombies.dto.player.PlayerSimpleDTO;
import com.example.human_vs_zombies.entities.*;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.squadMember.SquadMemberService;
import com.example.human_vs_zombies.services.user.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Mapping(target = "squadMember", source = "squadMember.squad_member_id")
    //@Mapping(target = "chat", source = "chat", qualifiedByName = "chatToMessageId")
    public abstract PlayerAdminDTO playerToPlayerAdminDTO(Player player);     //mapper for PlayerAdminDTO
    public abstract Collection<PlayerAdminDTO> playerToPlayerAdminDTO(Collection<Player> players);  //mapper for PlayerAdminDTO to handle a collection of Players
    public abstract PlayerSimpleDTO playerToPlayerSimpleDTO(Player player);  //mapper for PlayerSimpleDTO
    public abstract Collection<PlayerSimpleDTO> playerToPlayerSimpleDTO(Collection<Player> player);  //mapper for PlayerSimpleDTO to handle a collection of Players

    @Mapping(target = "user",source = "user", qualifiedByName = "UserIdToUser")
    @Mapping(target = "game",source = "game", qualifiedByName = "GameIdToGame")
    @Mapping(target = "squadMember",source = "squadMember", qualifiedByName = "SquadMemberIdToSquadMember")
    public abstract Player playerPostDTOtoPlayer(PlayerPostDTO playerPostDTO); //mapper to convert playerPostDTO to player
    @Mapping(target = "user",source = "user", qualifiedByName = "UserIdToUser")
    @Mapping(target = "game",source = "game", qualifiedByName = "GameIdToGame")
    @Mapping(target = "squadMember",source = "squadMember", qualifiedByName = "SquadMemberIdToSquadMember")
    public abstract Player playerPutDTOtoPlayer(PlayerPutDTO playerPutDTO); //mapper to convert playerPutDTO to player

    @Named(value = "killsToKillsId")
      Set<Integer> map(Set<Kill> value){
        if(value == null)
            return null;
        return value.stream()
                .map(s -> s.getKill_id())
                .collect(Collectors.toSet());
    }

    @Named("UserIdToUser")
    AppUser mapIdToUser(int id){ return userService.findById(id); }

    @Named("GameIdToGame")
    Game mapIdToGame(int id){ return gameService.findById(id); }

    @Named("SquadMemberIdToSquadMember")
    SquadMember mapIdToSquadMember(int id){ return squadMemberService.findById(id); }

//    @Named(value = "chatToMessageId")
//     Set<Integer> map(Set<Chat> value){
//        if(value == null)
//            return null;
//        return value.stream()
//                .map(s -> s.getMessage_id())
//                .collect(Collectors.toSet());
//    }
}
