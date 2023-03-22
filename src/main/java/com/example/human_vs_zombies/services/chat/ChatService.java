package com.example.human_vs_zombies.services.chat;

import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.enums.ChatScope;
import com.example.human_vs_zombies.services.CrudService;

import java.util.Collection;

public interface ChatService extends CrudService<Chat, Integer> {
    Collection<Chat> findAllChatByGameId(int game_id, ChatScope scope);

    int countMessagesOfGame(int gameId);
}
