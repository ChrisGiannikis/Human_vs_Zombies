package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.player.*;
import com.example.human_vs_zombies.dto.user.UserDTO;
import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.enums.State;
import com.example.human_vs_zombies.mappers.PlayerMapper;
import com.example.human_vs_zombies.mappers.UserMapper;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.player.PlayerService;
import com.example.human_vs_zombies.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Collection;
import java.util.Random;

import static java.util.Objects.isNull;

@RestController
@CrossOrigin
@RequestMapping("api/v1/games")
public class PlayerController {

    private final PlayerService playerService;

    private final UserService userService;
    private final GameService gameService;
    private final PlayerMapper playerMapper;
    private final UserMapper userMapper;
    private String roles ="";

    public PlayerController(PlayerService playerService, UserService userService, GameService gameService, PlayerMapper playerMapper, UserMapper userMapper) {
        this.playerService = playerService;
        this.userService = userService;
        this.gameService = gameService;
        this.playerMapper = playerMapper;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Get all players of a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerDTO.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID OR did not find any players in this game",
                    content = @Content)
    })
    @GetMapping("{game_id}/players")//GET: localhost:8080/api/v1/games/game_id/players
    public ResponseEntity getAllPlayers(@PathVariable int game_id, @RequestHeader int requestedByPlayerWithId, @AuthenticationPrincipal Jwt jwt){
        String arrayList = jwt.getClaimAsString("realm_access");
        if (arrayList.contains("ADMIN")) {

            Collection<PlayerNotAdminDTO> playerNotAdminDTOS = playerMapper.playerToPlayerAdminDTO(gameService.findById(game_id).getPlayers());
            if (playerNotAdminDTOS.isEmpty())
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(playerNotAdminDTOS);
        } else {
            PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findById(requestedByPlayerWithId));

            if (playerDTO.getGame() != game_id) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            Collection<PlayerDTO> playerDTOS = playerMapper.playerToPlayerSimpleDTO(gameService.findById(game_id).getPlayers());
            if (playerDTOS.isEmpty())
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(playerDTOS);
        }
    }

    @Operation(summary = "Get a player by ID, of a specific game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID, OR this game does not include a player of this ID",
                    content = @Content)

    })
    @GetMapping("{game_id}/players/{player_id}")//GET: localhost:8080/api/v1/games/game_id/players/player_id
    public ResponseEntity getPlayerById(@PathVariable int game_id, @PathVariable int player_id, @RequestHeader int requestedByPlayerWithId, @AuthenticationPrincipal Jwt jwt){
        String arrayList = jwt.getClaimAsString("realm_access");
        if (arrayList.contains("ADMIN")) {

            PlayerNotAdminDTO playerNotAdminDTO = playerMapper.playerToPlayerAdminDTO(playerService.findById(player_id));

            if (playerNotAdminDTO.getGame() != game_id) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(playerNotAdminDTO);
        } else {
            PlayerDTO requestedByPlayerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findById(requestedByPlayerWithId));

            if (requestedByPlayerDTO.getGame() != game_id) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findById(player_id));

            if (playerDTO.getGame() != game_id) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(playerDTO);
        }
    }

    @Operation(summary = "Add a player to a specific game")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Player successfully added",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID OR User does not exist with supplied ID",
                    content = @Content)
    })
    @PostMapping("{game_id}/players")//POST: localhost:8080/api/v1/games/game_id/players
    public ResponseEntity<PlayerDTO> addPlayerToGame(@RequestBody PlayerPostDTO playerPostDTO, @PathVariable int game_id){

        Game game = gameService.findById(game_id);

        if (isNull(game)) {
            return ResponseEntity.notFound().build();
        }

        if (game.getState() != State.REGISTRATION) {
            return ResponseEntity.badRequest().build();
        }

        UserDTO userDTO = userMapper.UserToUserDTO(userService.findById(playerPostDTO.getUser()));

        if (userDTO.getPlayer() != 0) {
            return ResponseEntity.badRequest().build();
        }
        Random random = new Random();
        boolean chance90true = random.nextInt(10) < 9;

        Player player = playerMapper.playerPostDTOtoPlayer(playerPostDTO);
        player.setHuman(chance90true);
        player.setPatient_zero(!chance90true);
        player.setGame(game);
        player.setBiteCode(RandomStringUtils.randomAlphanumeric(20).toUpperCase());
        playerService.add(player);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update player: Puts player to opponent team when game is on Registration State")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Player successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Game State: Completed or In Progress",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID OR this game does not include a player of this ID",
                    content = @Content)
    })
    @PutMapping({"{game_id}/players/{player_id}"})//PUT: localhost:8080/api/v1/games/game_id/players/player_id
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable int game_id, @PathVariable int player_id, @AuthenticationPrincipal Jwt jwt){

        roles = jwt.getClaimAsString("realm_access");
        if(!roles.contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Game game = gameService.findById(game_id);

        if(isNull(game)){
            return ResponseEntity.notFound().build();
        }

        if(game.getState() != State.REGISTRATION){
            return ResponseEntity.badRequest().build();
        }

        playerService.changeTeams(player_id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a player of a game by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Player successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID OR this game does not include a player of this ID",
                    content = @Content)
    })
    @DeleteMapping({"{game_id}/players/{player_id}"})//DELETE: localhost:8080/api/v1/games/game_id/players/player_id
    public ResponseEntity<PlayerDTO> deletePlayer(@PathVariable int game_id, @PathVariable int player_id, @AuthenticationPrincipal Jwt jwt){

        roles = jwt.getClaimAsString("realm_access");
        if(!roles.contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findById(player_id));

        if(playerDTO.getGame() != game_id){
            return ResponseEntity.notFound().build();
        }


        playerService.deleteById(player_id);

        return ResponseEntity.noContent().build();
    }
}
