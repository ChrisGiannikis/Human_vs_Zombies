package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.game.GameDTO;
import com.example.human_vs_zombies.dto.game.GamePostDTO;
import com.example.human_vs_zombies.dto.game.GamePutDTO;
import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.enums.State;
import com.example.human_vs_zombies.mappers.*;
import com.example.human_vs_zombies.services.game.GameService;
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

import java.net.URI;
import java.util.Collection;

import static java.util.Objects.isNull;
@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/games")
public class GameController {
    private final GameService gameService;
    private final GameMapper gameMapper;
    private String roles ="";

    public GameController(GameService gameService, GameMapper gameMapper){
        this.gameService = gameService;
        this.gameMapper = gameMapper;
    }

    @Operation(summary = "Get all games")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDTO.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Did not find any games",
                    content = @Content)
    })
    @GetMapping//GET: localhost:8080/api/v1/games
    public ResponseEntity<Collection<GameDTO>> getAllGames(){
        Collection<GameDTO> gameDTOS = gameMapper.gameToGameDto(gameService.findAll());
        if(gameDTOS.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(gameDTOS);
    }

    @Operation(summary = "Get a game by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID",
                    content = @Content)

    })
    @GetMapping("{game_id}")//GET: localhost:8080/api/v1/games/id
    public ResponseEntity<GameDTO> getGameById(@PathVariable int game_id){
        GameDTO gameDTO = gameMapper.gameToGameDto(gameService.findById(game_id));
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
    public ResponseEntity<GamePostDTO> addGame(@RequestBody GamePostDTO gamePostDTO, @AuthenticationPrincipal Jwt jwt){

        roles = jwt.getClaimAsString("realm_access");
        if(!roles.contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (isNull(gamePostDTO.getName()))
            return ResponseEntity.badRequest().build();
        Game game = gameMapper.gamePostDtoToGame(gamePostDTO);
        game.setState(State.REGISTRATION);
        game = gameService.add(game);
        URI location = URI.create("api/v1/games/" + game.getGame_id());
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Updates a game")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Game successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Game completed, cannot be updated",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID",
                    content = @Content)
    })
    @PutMapping("{game_id}")//PUT: localhost:8080/api/v1/games/id
    public ResponseEntity<GamePutDTO> updateGame(@RequestBody GamePutDTO gamePutDTO, @PathVariable int game_id, @RequestHeader State state, @AuthenticationPrincipal Jwt jwt){

        roles = jwt.getClaimAsString("realm_access");
        if(!roles.contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        GameDTO gameDTO = gameMapper.gameToGameDto(gameService.findById(game_id));

        if(isNull(gameDTO)){
            return ResponseEntity.notFound().build();
        }

        if(state == State.REGISTRATION || gameDTO.getState()==State.COMPLETED){
            return ResponseEntity.badRequest().build();
        }

        Game game = gameMapper.gamePutDtoToGame(gamePutDTO);
        game.setGame_id(game_id);
        game.setState(state);
        gameService.update(game);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a game by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Game successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID",
                    content = @Content)
    })
    @DeleteMapping({"{game_id}"})//DELETE: localhost:8080/api/v1/games/id
    public ResponseEntity<GameDTO> deleteGame(@PathVariable int game_id, @AuthenticationPrincipal Jwt jwt){

        roles = jwt.getClaimAsString("realm_access");
        if(!roles.contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if(isNull(gameService.findById(game_id))){
            return ResponseEntity.notFound().build();
        }
        gameService.deleteById(game_id);
        return ResponseEntity.noContent().build();
    }
}
