package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.ChatDTO;
import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.mappers.ChatMapper;
import com.example.human_vs_zombies.services.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping(path="api/v1/chats")
public class ChatController {

    private final ChatService chatService;
    private final ChatMapper chatMapper;

    public ChatController(ChatService chatService, ChatMapper chatMapper){
        this.chatService = chatService;
        this.chatMapper = chatMapper;
    }


    @GetMapping//GET: localhost:8080/api/v1/chats
    public ResponseEntity<Collection<ChatDTO>> getAll(){
        Collection<ChatDTO> chatDTOS = chatMapper.chatToChatDto(chatService.findAll());
        return ResponseEntity.ok(chatDTOS);
    }


    @GetMapping("{id}")//GET: localhost:8080/api/v1/chats/id
    public ResponseEntity<ChatDTO> getById(@PathVariable int id){
        ChatDTO chatDTO = chatMapper.chatToChatDto(chatService.findById(id));
        return ResponseEntity.ok(chatDTO);
    }

    @PostMapping // POST: localhost:8080/api/v1/chats
    public ResponseEntity<ChatDTO> add(@RequestBody ChatDTO chatDTO) {
        chatService.add(chatMapper.chatDtoToChat(chatDTO));
        URI location = URI.create("chats/" + chatDTO.getMessage_id());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}") // POST: localhost:8080/api/v1/chats
    public ResponseEntity<ChatDTO> update(@RequestBody ChatDTO chatDTO, @PathVariable int id) {
        if(id != chatDTO.getMessage_id())
            return ResponseEntity.badRequest().build();
        chatService.update(chatMapper.chatDtoToChat(chatDTO));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}") // DELETE: localhost:8080/api/v1/chats/id
    public ResponseEntity<ChatDTO> delete(@PathVariable int id) {
        chatService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
