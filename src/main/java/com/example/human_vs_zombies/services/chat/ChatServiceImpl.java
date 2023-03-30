package com.example.human_vs_zombies.services.chat;

import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.enums.ChatScope;
import com.example.human_vs_zombies.exceptions.ChatNotFoundException;
import com.example.human_vs_zombies.repositories.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class ChatServiceImpl implements ChatService{

    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository){
        this.chatRepository=chatRepository;
    }
    @Override
    public Chat findById(Integer id) {
        return chatRepository.findById(id).orElseThrow(() -> new ChatNotFoundException(id));
    }

    @Override
    public Collection<Chat> findAll() {
        return chatRepository.findAll();
    }

    @Override
    public Collection<Chat> findAllNonSquadChatByGameId(int gameId, boolean isHuman) {
        return chatRepository.findAllNonSquadChatByGameId(gameId, ChatScope.GLOBAL, ChatScope.FACTION, isHuman);
    }

    @Override
    public Collection<Chat> findAllNonSquadChatByGameIdAdmin(int gameId) {
        return chatRepository.findAllNonSquadChatByGameIdAdmin(gameId, ChatScope.SQUAD);
    }

    @Override
    public Collection<Chat> findAllSquadChatByGameId(int gameId, int squadId) {
        return chatRepository.findAllSquadChatByGameId(gameId, squadId, ChatScope.SQUAD);
    }

    @Override
    public void addSquadChat(Chat chat) {
        Set<Chat> chats = chat.getPlayer().getChat();
        chats.add(chat);
        chat.getPlayer().setChat(chats);

        Set<Chat> squadChats = chat.getSquad().getChat();
        squadChats.add(chat);
        chat.getSquad().setChat(squadChats);

        chatRepository.save(chat);
    }

    @Override
    public Chat add(Chat chat) {
        Set<Chat> chats = chat.getPlayer().getChat();
        chats.add(chat);
        chat.getPlayer().setChat(chats);

        return chatRepository.save(chat);
    }

    @Override
    public Chat update(Chat entity) {
        return chatRepository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        this.findById(id);
        if(chatRepository.existsById(id)) {
            chatRepository.deleteById(id);
        }
    }
}
