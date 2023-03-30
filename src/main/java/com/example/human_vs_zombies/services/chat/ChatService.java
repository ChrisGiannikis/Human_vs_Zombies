package com.example.human_vs_zombies.services.chat;

import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.services.CrudService;

import java.util.Collection;

public interface ChatService extends CrudService<Chat, Integer> {
    void addSquadChat(Chat chat);

    Collection<Chat> findAllSquadChatByGameId(int gameId, int squadId);

    Collection<Chat> findAllNonSquadChatByGameId(int gameId, boolean isHuman);

    Collection<Chat> findAllNonSquadChatByGameIdAdmin(int gameId);
}
