package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.chat.ChatDTO;
import com.example.human_vs_zombies.dto.chat.ChatPostDTO;
import com.example.human_vs_zombies.dto.kill.KillDTO;
import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.entities.SquadMember;
import com.example.human_vs_zombies.enums.ChatScope;
import com.example.human_vs_zombies.mappers.ChatMapper;
import com.example.human_vs_zombies.services.chat.ChatService;
import com.example.human_vs_zombies.services.player.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
@CrossOrigin
@RequestMapping(path="api/v1/games")
public class ChatController {

    private final ChatService chatService;
    private final PlayerService playerService;
    private final ChatMapper chatMapper;
    private String roles ="";

    public ChatController(ChatService chatService, PlayerService playerService, ChatMapper chatMapper){
        this.chatService = chatService;
        this.playerService = playerService;
        this.chatMapper = chatMapper;
    }

    @Operation(summary = "Get all chat of a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChatDTO.class)) }),
            @ApiResponse(responseCode = "400",
                    description = "Can't provide squad messages",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Did not find any messages",
                    content = @Content)
    })
    @GetMapping("{game_id}/chat")//GET: localhost:8080/api/v1/games/game_id/chat
    public ResponseEntity<Collection<ChatDTO>> getAllChat(@PathVariable int game_id, @RequestHeader int requestedByPlayerWithId, @AuthenticationPrincipal Jwt jwt){//@RequestHeader ChatScope scope){

        String arrayList = jwt.getClaimAsString("roles");
        if(arrayList.contains("ADMIN")) {
            Collection<ChatDTO> chatDTOS = chatMapper.chatToChatDto(chatService.findAllNonSquadChatByGameIdAdmin(game_id));

            if (chatDTOS.isEmpty())
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(chatDTOS);
        }

        Player player = playerService.findById(requestedByPlayerWithId);

        if(player.getGame().getGame_id()!=game_id){
            return ResponseEntity.notFound().build();
        }

        boolean isHuman = player.isHuman();
        Collection<ChatDTO> chatDTOS = chatMapper.chatToChatDto(chatService.findAllNonSquadChatByGameId(game_id, isHuman));

        if(chatDTOS.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(chatDTOS);
    }

    @Operation(summary = "Send a new message into a specific game")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Message successfully sent",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID OR player with supplied ID does not exist in this game",
                    content = @Content)
    })
    @PostMapping("{game_id}/chat")//POST: localhost:8080/api/v1/games/game_id/chat
    public ResponseEntity<KillDTO> sendMessage(@RequestBody ChatPostDTO chatPostDTO, @PathVariable int game_id, @RequestHeader ChatScope scope){

        if(playerService.findById(chatPostDTO.getPlayer()).getGame().getGame_id()!=game_id){
            return ResponseEntity.notFound().build();
        }

        if(scope==ChatScope.SQUAD){
            return ResponseEntity.badRequest().build();
        }

        if (chatPostDTO.getPlayer()==0){
            return ResponseEntity.badRequest().build();
        }

        Chat chat = chatMapper.chatPostDtoToChat(chatPostDTO);

        chat.setChatScope(scope);

        chatService.add(chat);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all chat of a specific squad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChatDTO.class)) }),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Did not find any messages",
                    content = @Content)
    })
    @GetMapping("{game_id}/squads/{squad_id}/chat")//GET: localhost:8080/api/v1/games/game_id/squads/squad_id/chat
    public ResponseEntity<Collection<ChatDTO>> getAllSquadChat(@PathVariable int game_id, @PathVariable int squad_id, @RequestHeader int requestedByPlayerWithId, @AuthenticationPrincipal Jwt jwt){

        String roles = jwt.getClaimAsString("realm_access");
        if (roles.contains("ADMIN")) {
            Collection<ChatDTO> chatDTOS = chatMapper.chatToChatDto(chatService.findAllSquadChatByGameId(game_id, squad_id));

            if(chatDTOS.isEmpty())
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(chatDTOS);
        }

        if(playerService.findById(requestedByPlayerWithId).getSquadMember().getSquad().getSquad_id() != squad_id){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Collection<ChatDTO> chatDTOS = chatMapper.chatToChatDto(chatService.findAllSquadChatByGameId(game_id, squad_id));

        if(chatDTOS.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(chatDTOS);
    }


    @Operation(summary = "Send a new message to your squad")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Message successfully sent",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID OR player with supplied ID does not exist in this game",
                    content = @Content)
    })
    @PostMapping("{game_id}/squads/{squad_id}/chat")//POST: localhost:8080/api/v1/games/game_id/squads/squad_id/chat
    public ResponseEntity<KillDTO> sendMessageToSquad(@RequestBody ChatPostDTO chatPostDTO, @PathVariable int game_id, @PathVariable int squad_id, @AuthenticationPrincipal Jwt jwt){

        roles = jwt.getClaimAsString("realm_access");
        if(!roles.contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Player player = playerService.findById(chatPostDTO.getPlayer()) ;

        if(player.getGame().getGame_id()!=game_id){
            return ResponseEntity.notFound().build();
        }

        SquadMember squadMember = player.getSquadMember();

        if (chatPostDTO.getPlayer()==0){
            return ResponseEntity.badRequest().build();
        }

        if(squadMember.getSquad().getSquad_id() != squad_id){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Chat chat = chatMapper.chatPostDtoToChat(chatPostDTO);

        chat.setSquad(squadMember.getSquad());
        chat.setChatScope(ChatScope.SQUAD);

        chatService.addSquadChat(chat);
        return ResponseEntity.ok().build();
    }

}
