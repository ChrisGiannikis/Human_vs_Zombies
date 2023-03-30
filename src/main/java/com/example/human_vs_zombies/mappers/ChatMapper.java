package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.chat.ChatDTO;
import com.example.human_vs_zombies.dto.chat.ChatPostDTO;
import com.example.human_vs_zombies.entities.AppUser;
import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.entities.Squad;
import com.example.human_vs_zombies.services.player.PlayerService;
import com.example.human_vs_zombies.services.squad.SquadService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class ChatMapper {
    @Autowired
    protected PlayerService playerService;
    @Autowired
    protected SquadService squadService;

    @Mapping(target = "player_name", source = "player.player_id", qualifiedByName = "UserNamesToFullName")
    @Mapping(target = "squad", source = "squad.squad_id")
    public abstract ChatDTO chatToChatDto(Chat chat);

    @Mapping(target = "player", source = "player", qualifiedByName = "playerIdToPlayer")
    @Mapping(target = "message_id", ignore = true)
    @Mapping(target = "squad", ignore = true)
    @Mapping(target = "chatScope", ignore = true)
    public abstract Chat chatPostDtoToChat(ChatPostDTO chatPostDTO);

    @Named("playerIdToPlayer")
    Player mapIdToPlayer(int id){
        return playerService.findById(id);
    }

    @Named("UserNamesToFullName")
    String mapNames(Integer id){
        Player player = playerService.findById(id);
        AppUser user = player.getUser();
        return user.getFirst_name() + " " + user.getLast_name();
    }

    public abstract Collection<ChatDTO> chatToChatDto(Collection<Chat> chats);
}
