package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.ChatDTO;
import com.example.human_vs_zombies.dto.ChatPostDTO;
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

    @Mapping(target = "player", source = "player.player_id")
    @Mapping(target = "squad", source = "squad.squad_id")
    public abstract ChatDTO chatToChatDto(Chat chat);

    @Mapping(target = "player", source = "player", qualifiedByName = "playerIdToPlayer")
    @Mapping(target = "squad", source = "squad", qualifiedByName = "squadIdToSquad")
    public abstract Chat chatDtoToChat(ChatDTO chatDTO);

    @Mapping(target = "player", source = "player", qualifiedByName = "playerIdToPlayer")
    @Mapping(target = "message_id", ignore = true)
    @Mapping(target = "squad", ignore = true)
    @Mapping(target = "chatScope", ignore = true)
//    @Mapping(target = "squad", source = "player.squad", qualifiedByName = "squadIdToSquad")
    public abstract Chat chatPostDtoToChat(ChatPostDTO chatPostDTO);

    @Named("playerIdToPlayer")
    Player mapIdToPlayer(int id){
        return playerService.findById(id);
    }

    @Named("squadIdToSquad")
    Squad mapIdToSquad(Integer id){
        if (id != null) {
            return squadService.findById(id);
        }
        return null;
    }

    public abstract Collection<ChatDTO> chatToChatDto(Collection<Chat> chats);
}
