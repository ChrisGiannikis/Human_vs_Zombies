package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.user.UserDTO;
import com.example.human_vs_zombies.dto.user.UserPostDTO;
import com.example.human_vs_zombies.dto.user.UserPutDTO;
import com.example.human_vs_zombies.entities.AppUser;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO UserToUserDTO(AppUser user);
    Collection<UserDTO> UserToUserDTO(Collection<AppUser> users);
    AppUser UserDTOToUser(UserDTO userDTO);
    AppUser UserPostDTOToUser(UserPostDTO userPostDTO);
    AppUser UserPutDTOToUser(UserPutDTO userPutDTO);
}
