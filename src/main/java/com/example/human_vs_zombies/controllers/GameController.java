package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.GameDTO;
import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.mappers.GameMapper;
import com.example.human_vs_zombies.services.game.GameService;
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
@RequestMapping(path = "api/v1/games")
public class GameController {

    private final GameService gameService;
    private final GameMapper gameMapper;

    public GameController(GameService gameService, GameMapper gameMapper){
        this.gameService = gameService;
        this.gameMapper = gameMapper;
    }

    @Operation(summary = "Get all games")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Did not find any games",
                    content = @Content)
    })
    @GetMapping//GET: localhost:8080/api/v1/games
    public ResponseEntity<Collection<GameDTO>> getAll(){
        Collection<GameDTO> gameDTOS = gameMapper.gameToGameDto(gameService.findAll());
        return ResponseEntity.ok(gameDTOS);
    }

    @Operation(summary = "Get a game by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID",
                    content = @Content)

    })
    @GetMapping("{id}")//GET: localhost:8080/api/v1/games/id
    public ResponseEntity<GameDTO> getById(@PathVariable int id){
        GameDTO gameDTO = gameMapper.gameToGameDto(gameService.findById(id));
        return ResponseEntity.ok(gameDTO);
    }

    @Operation(summary = "Add a game")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Game successfully added",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
    })
    @PostMapping//POST: localhost:8080/api/v1/games
    public ResponseEntity<GameDTO> add(@RequestBody GameDTO gameDTO){
        gameService.add(gameMapper.gameDtoToGame(gameDTO));
        URI location = URI.create("games/" + gameDTO.getGame_id());
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Updates a game")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Game successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID",
                    content = @Content)
    })
    @PutMapping({"id"})//PUT: localhost:8080/api/v1/games/id
    public ResponseEntity<GameDTO> update(@RequestBody GameDTO gameDTO, @PathVariable int id){
        if(id!=gameDTO.getGame_id()){
            return ResponseEntity.badRequest().build();
        }

        gameService.update(gameMapper.gameDtoToGame(gameDTO));

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a game by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Game successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID",
                    content = @Content)
    })
    @DeleteMapping({"{id}"})//DELETE: localhost:8080/api/v1/games/id
    public ResponseEntity<GameDTO> delete(@PathVariable int id){
        GameDTO gameDTO = gameMapper.gameToGameDto(gameService.findById(id));
        gameService.deleteById(gameDTO.getGame_id());
        return ResponseEntity.noContent().build();
    }
}
