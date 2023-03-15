package com.example.human_vs_zombies.services.chat;

import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.exceptions.ChatNotFoundException;
import com.example.human_vs_zombies.repositories.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

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
    public Chat add(Chat entity) {
        return chatRepository.save(entity);
    }

    @Override
    public Chat update(Chat entity) {
        return chatRepository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        if(chatRepository.existsById(id)) {
            chatRepository.deleteById(id);
        }
    }
}
