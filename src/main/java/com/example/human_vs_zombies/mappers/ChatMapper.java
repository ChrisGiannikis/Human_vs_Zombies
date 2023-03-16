package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.ChatDTO;
import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.entities.Squad;
import com.example.human_vs_zombies.services.player.PlayerService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class ChatMapper {

    @Autowired
    protected PlayerService playerService;

    @Mapping(target = "player", source = "player.player_id")
    @Mapping(target = "squad", source = "squad.squad_id")
    public abstract ChatDTO chatToChatDto(Chat chat);

    @Mapping(target = "player", source = "player", qualifiedByName = "playerIdToPlayer")
//    @Mapping(target = "squad", source = "squad", qualifiedByName = "squadIdToSquad")
    @Mapping(target = "squad", ignore = true)
    public abstract Chat chatDtoToChat(ChatDTO chatDTO);

    @Named("playerIdToPlayer")
    Player mapIdToPlayer(int id){
        return playerService.findById(id);
    }

//    @Named("squadIdToSquad")
//    Squad mapIdToSquad(int id){
//
//    }

    public abstract Collection<ChatDTO> chatToChatDto(Collection<Chat> chats);
}
