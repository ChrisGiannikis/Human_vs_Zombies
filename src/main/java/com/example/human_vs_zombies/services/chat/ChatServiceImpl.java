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
    public Collection<Chat> findAllChatByGameId(int game_id, ChatScope scope) {
        Collection<Chat> chat = this.findAll();
        chat.removeIf(ch -> ch.getPlayer().getGame().getGame_id()!=game_id || ch.getChatScope() != scope);
        return chat;
    }

    @Override
    public int countMessagesOfGame(int gameId) {
        return chatRepository.countMessagesOfGame(gameId, ChatScope.SQUAD);
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
