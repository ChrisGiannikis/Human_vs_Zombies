package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.chat.ChatDTO;
import com.example.human_vs_zombies.dto.chat.ChatPostDTO;
import com.example.human_vs_zombies.dto.chat.ChatPutDTO;
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


    @Operation(summary = "Get all messages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Did not find any messages",
                    content = @Content)
    })
    @GetMapping//GET: localhost:8080/api/v1/chats
    public ResponseEntity<Collection<ChatDTO>> getAll(){
        Collection<ChatDTO> chatDTOS = chatMapper.chatToChatDto(chatService.findAll());
        if(chatDTOS.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(chatDTOS);
    }

    @Operation(summary = "Get a message by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Message does not exist with supplied ID",
                    content = @Content)

    })
    @GetMapping("{id}")//GET: localhost:8080/api/v1/chats/id
    public ResponseEntity<ChatDTO> getById(@PathVariable int id){
        ChatDTO chatDTO = chatMapper.chatToChatDto(chatService.findById(id));
        return ResponseEntity.ok(chatDTO);
    }

    @Operation(summary = "Add a message")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Message successfully added",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
    })
    @PostMapping // POST: localhost:8080/api/v1/chats
    public ResponseEntity<ChatDTO> add(@RequestBody ChatPostDTO chatPostDTO) {
        chatService.add(chatMapper.chatPostDtoToChat(chatPostDTO));
        int chat_id = chatMapper.chatPostDtoToChat(chatPostDTO).getMessage_id();
        URI location = URI.create("chats/" + chat_id);
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Updates a message")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Message successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Message not found with supplied ID",
                    content = @Content)
    })
    @PutMapping("{id}") // POST: localhost:8080/api/v1/chats
    public ResponseEntity<ChatDTO> update(@RequestBody ChatPutDTO chatPutDTO, @PathVariable int id) {
        if(id != chatPutDTO.getMessage_id())
            return ResponseEntity.badRequest().build();
        chatService.update(chatMapper.chatPutDtoToChat(chatPutDTO));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a message by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Message successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Message not found with supplied ID",
                    content = @Content)
    })
    @DeleteMapping("{id}") // DELETE: localhost:8080/api/v1/chats/id
    public ResponseEntity<ChatDTO> delete(@PathVariable int id) {
        chatService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
