package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.services.chat.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping(path="api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    @GetMapping("{id}")//GET: localhost:8080/api/v1/chats/id
    public ResponseEntity<Chat> getById(@PathVariable int id){
        return ResponseEntity.ok(chatService.findById(id));
    }

    @GetMapping//GET: localhost:8080/api/v1/chats
    public ResponseEntity<Collection<Chat>> getAll(){
        return ResponseEntity.ok(chatService.findAll());
    }

    @PostMapping // POST: localhost:8080/api/v1/chats
    public ResponseEntity<Chat> add(@RequestBody Chat chat) {
        Chat ch = chatService.add(chat);
        URI location = URI.create("chats/" + ch.getMessage_id());
        return ResponseEntity.created(location).build();
        // return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{id}") // POST: localhost:8080/api/v1/chats
    public ResponseEntity<Chat> update(@RequestBody Chat chat, @PathVariable int id) {
        // Validates if body is correct
        if(id != chat.getMessage_id())
            return ResponseEntity.badRequest().build();
        chatService.update(chat);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}") // DELETE: localhost:8080/api/v1/chats/id
    public ResponseEntity<Chat> delete(@PathVariable int id) {
        chatService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
