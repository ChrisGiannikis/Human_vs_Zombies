package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.user.UserDTO;
import com.example.human_vs_zombies.dto.user.UserPostDTO;
import com.example.human_vs_zombies.dto.user.UserPutDTO;
import com.example.human_vs_zombies.entities.AppUser;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.services.player.PlayerService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    protected PlayerService playerService;
    @Mapping(target = "player", source = "player.player_id")
    public abstract UserDTO UserToUserDTO(AppUser user);
    public abstract Collection<UserDTO> UserToUserDTO(Collection<AppUser> users);

    @Mapping(target = "player", source = "player", qualifiedByName = "playerIdToPlayer")
    public abstract AppUser UserDTOToUser(UserDTO userDTO);
    public abstract AppUser UserPostDTOToUser(UserPostDTO userPostDTO);
    public abstract AppUser UserPutDTOToUser(UserPutDTO userPutDTO);

    @Named("playerIdToPlayer")
    Player mapIdToPlayer(int id){
        return playerService.findById(id);
    }
}
