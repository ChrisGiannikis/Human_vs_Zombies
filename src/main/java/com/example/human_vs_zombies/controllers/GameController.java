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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

import static java.util.Objects.isNull;
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
    public ResponseEntity<GamePostDTO> addGame(@RequestBody GamePostDTO gamePostDTO){
        if (isNull(gamePostDTO.getName()))
            return ResponseEntity.badRequest().build();
        Game game = gameMapper.gamePostDtoToGame(gamePostDTO);
        game.setState(State.REGISTRATION);
        gameService.add(game);
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
    public ResponseEntity<GamePutDTO> updateGame(@RequestBody GamePutDTO gamePutDTO, @PathVariable int game_id, @RequestHeader State state){

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
    public ResponseEntity<GameDTO> deleteGame(@PathVariable int game_id){
        if(isNull(gameService.findById(game_id))){
            return ResponseEntity.notFound().build();
        }
        gameService.deleteById(game_id);
        return ResponseEntity.noContent().build();
    }

//    @Operation(summary = "Get all chat of a game")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200",
//                    description = "Success",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = MissionDTO.class)) }),
//            @ApiResponse(responseCode = "404",
//                    description = "Did not find any messages",
//                    content = @Content)
//    })
//    @GetMapping("{game_id}/chat")//GET: localhost:8080/api/v1/games/game_id/chat
//    public ResponseEntity<Collection<ChatDTO>> getAllChat(@PathVariable int game_id, @RequestHeader ChatScope scope){
//
//        Collection<ChatDTO> chatDTOS = chatMapper.chatToChatDto(chatService.findAll());
//        Collection<PlayerDTO> playerDTOS = playerMapper.playerToPlayerSimpleDTO(playerService.findAll());
//        playerDTOS.removeIf(player -> player.getGame()!=game_id);
//        chatDTOS.removeIf(chat -> chat.getChatScope() != scope || !playerDTOS.contains(playerMapper.playerToPlayerSimpleDTO(playerService.findById(chat.getPlayer()))));
//
//        if(chatDTOS.isEmpty())
//            return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(chatDTOS);
//    }

//    @Operation(summary = "Send a new message into a specific game")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "201",
//                    description = "Message successfully sent",
//                    content = @Content),
//            @ApiResponse(responseCode = "400",
//                    description = "Malformed request",
//                    content = @Content),
//            @ApiResponse(responseCode = "401",
//                    description = "Bad request",
//                    content = @Content),
//            @ApiResponse(responseCode = "404",
//                    description = "Game does not exist with supplied ID",
//                    content = @Content)
//    })
//    @PostMapping("{game_id}/chat")//POST: localhost:8080/api/v1/games/game_id/chat
//    public ResponseEntity<KillDTO> sendMessage(@RequestBody ChatPostDTO chatPostDTO, @PathVariable int game_id, @RequestHeader ChatScope scope){
//
//        if(isNull(gameService.findById(game_id))){
//            return ResponseEntity.notFound().build();
//        }
//
//        if (chatPostDTO.getPlayer()==0){
//            return ResponseEntity.badRequest().build();
//        }
//
//        Chat chat = chatMapper.chatPostDtoToChat(chatPostDTO);
//
//        chat.setChatScope(scope);
//
//        if(chat.getChatScope()==ChatScope.SQUAD){
//            chat.setSquad(playerService.findById(chatPostDTO.getPlayer()).getSquadMember().getSquad());
//        }
//
//        chatService.add(chat);
//        URI location = URI.create("api/v1/games/" + game_id + "/chat/" + chat.getMessage_id());
//        return ResponseEntity.created(location).build();
//    }

//    @Operation(summary = "Get all players of a game")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200",
//                    description = "Success",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = PlayerDTO.class)) }),
//            @ApiResponse(responseCode = "404",
//                    description = "Game does not exist with supplied ID OR did not find any players in this game",
//                    content = @Content)
//    })
//    @GetMapping("{game_id}/players")//GET: localhost:8080/api/v1/games/game_id/players
//    public ResponseEntity<Collection<PlayerDTO>> getAllPlayers(@PathVariable int game_id){
//        Collection<PlayerDTO> playerDTOS = playerMapper.playerToPlayerSimpleDTO(gameService.findById(game_id).getPlayers());
//        if(playerDTOS.isEmpty())
//            return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(playerDTOS);
//    }

//    @Operation(summary = "Get a player by ID, of a specific game")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200",
//                    description = "Success",
//                    content = {@Content(mediaType = "application/json",
//                            schema = @Schema(implementation = PlayerDTO.class))}),
//            @ApiResponse(responseCode = "404",
//                    description = "Game does not exist with supplied ID, OR this game does not include a player of this ID",
//                    content = @Content)
//
//    })
//    @GetMapping("{game_id}/players/{player_id}")//GET: localhost:8080/api/v1/games/game_id/players/player_id
//    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable int game_id, @PathVariable int player_id){
//        PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(gameService.findPlayerById(game_id,player_id));
//
//        if(isNull(playerDTO)){
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(playerDTO);
//    }

//    @Operation(summary = "Add a player to a specific game")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "201",
//                    description = "Player successfully added",
//                    content = @Content),
//            @ApiResponse(responseCode = "400",
//                    description = "Malformed request",
//                    content = @Content),
//            @ApiResponse(responseCode = "404",
//                    description = "Game does not exist with supplied ID OR User does not exist with supplied ID",
//                    content = @Content)
//    })
//    @PostMapping("{game_id}/players")//POST: localhost:8080/api/v1/games/game_id/players
//    public ResponseEntity<PlayerDTO> addPlayerToGame(@RequestBody PlayerPostDTO playerPostDTO, @PathVariable int game_id){
//
//        if(isNull(gameService.findById(game_id))){
//            return ResponseEntity.notFound().build();
//        }
//
//        if ((playerPostDTO.isHuman() && playerPostDTO.isPatient_zero())){
//            return ResponseEntity.badRequest().build();
//        }
//
//        Player player = playerMapper.playerPostDTOtoPlayer(playerPostDTO);
//        gameService.addPlayer(game_id, player);
//        URI location = URI.create("api/v1/games/" + game_id + "/players/" + player.getPlayer_id());
//        return ResponseEntity.created(location).build();
//    }

//    @Operation(summary = "Updates a player")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "204",
//                    description = "Player successfully updated",
//                    content = @Content),
//            @ApiResponse(responseCode = "400",
//                    description = "Malformed request",
//                    content = @Content),
//            @ApiResponse(responseCode = "404",
//                    description = "Game not found with supplied ID OR this game does not include a player of this ID",
//                    content = @Content)
//    })
//    @PutMapping({"{game_id}/players/{player_id}"})//PUT: localhost:8080/api/v1/games/game_id/players/player_id
//    public ResponseEntity<PlayerDTO> updatePlayer(@RequestBody PlayerPutDTO playerPutDTO, @PathVariable int game_id, @PathVariable int player_id){
//
//        if(isNull(gameService.findById(game_id))){
//            return ResponseEntity.notFound().build();
//        }
//
//        if ((playerPutDTO.isHuman() && playerPutDTO.isPatient_zero())){
//            return ResponseEntity.badRequest().build();
//        }
//
//        Player player = playerMapper.playerPutDTOtoPlayer(playerPutDTO); //ask for put dto
//        gameService.updatePlayer(game_id, player_id, player);
//
//        return ResponseEntity.noContent().build();
//    }

//    @Operation(summary = "Delete a player of a game by ID")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "204",
//                    description = "Player successfully deleted",
//                    content = @Content),
//            @ApiResponse(responseCode = "404",
//                    description = "Game not found with supplied ID OR this game does not include a player of this ID",
//                    content = @Content)
//    })
//    @DeleteMapping({"{game_id}/players/{player_id}"})//DELETE: localhost:8080/api/v1/games/game_id/players/player_id
//    public ResponseEntity<PlayerDTO> deletePlayer(@PathVariable int game_id, @PathVariable int player_id){
//
//        if(isNull(gameService.findById(game_id)) || !gameService.findById(game_id).getPlayers().contains(playerService.findById(player_id))){
//            return ResponseEntity.notFound().build();
//        }
//
//        playerService.deleteById(player_id);
//
//        return ResponseEntity.noContent().build();
//    }

//    @Operation(summary = "Get all missions of a game")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200",
//                    description = "Success",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = PlayerDTO.class)) }),
//            @ApiResponse(responseCode = "404",
//                    description = "Game does not exist with supplied ID OR did not find any missions in this game",
//                    content = @Content)
//    })
//    @GetMapping("{game_id}/missions")//GET: localhost:8080/api/v1/games/game_id/missions
//    public ResponseEntity<Collection<MissionDTO>> getAllMissions(@PathVariable int game_id){
//        Collection<MissionDTO> missionDTOS = missionMapper.missionToMissionDTO(gameService.findById(game_id).getMissions());
//        if(missionDTOS.isEmpty())
//            return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(missionDTOS);
//    }


//    @Operation(summary = "Get a mission by ID, of a specific game")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200",
//                    description = "Success",
//                    content = {@Content(mediaType = "application/json",
//                            schema = @Schema(implementation = MissionDTO.class))}),
//            @ApiResponse(responseCode = "404",
//                    description = "Game does not exist with supplied ID, OR this game does not include a mission of this ID",
//                    content = @Content)
//
//    })
//    @GetMapping("{game_id}/missions/{mission_id}")//GET: localhost:8080/api/v1/games/game_id/missions/mission_id
//    public ResponseEntity<MissionDTO> getMissionById(@PathVariable int game_id, @PathVariable int mission_id){
//        MissionDTO missionDTO = missionMapper.missionToMissionDTO(gameService.findMissionById(game_id, mission_id));
//
//        if(isNull(missionDTO)){
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(missionDTO);
//    }

//    @Operation(summary = "Add a mission to a specific game")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "201",
//                    description = "Mission successfully added",
//                    content = @Content),
//            @ApiResponse(responseCode = "400",
//                    description = "Malformed request",
//                    content = @Content),
//            @ApiResponse(responseCode = "404",
//                    description = "Game does not exist with supplied ID",
//                    content = @Content)
//    })
//    @PostMapping("{game_id}/missions")//POST: localhost:8080/api/v1/games/game_id/missions
//    public ResponseEntity<MissionDTO> addMissionToGame(@RequestBody MissionPostDTO missionPostDTO, @PathVariable int game_id){
//
//        if(isNull(gameService.findById(game_id))){
//            return ResponseEntity.notFound().build();
//        }
//
//        if ((!missionPostDTO.isHuman_visible() && !missionPostDTO.isZombie_visible()) || isNull(missionPostDTO.getName())){
//            return ResponseEntity.badRequest().build();
//        }
//
//        Mission mission = missionMapper.missionPostDTOToMission(missionPostDTO);
//        gameService.addMission(game_id, mission);
//        URI location = URI.create("api/v1/games/" + game_id + "/missions/" + mission.getMission_id());
//        return ResponseEntity.created(location).build();
//    }


//    @Operation(summary = "Updates a mission")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "204",
//                    description = "Mission successfully updated",
//                    content = @Content),
//            @ApiResponse(responseCode = "400",
//                    description = "Malformed request",
//                    content = @Content),
//            @ApiResponse(responseCode = "404",
//                    description = "Game not found with supplied ID OR this game does not include a mission of this ID",
//                    content = @Content)
//    })
//    @PutMapping({"{game_id}/missions/{mission_id}"})//PUT: localhost:8080/api/v1/games/game_id/missions/mission_id
//    public ResponseEntity<MissionDTO> updateMission(@RequestBody MissionPutDTO missionPutDTO, @PathVariable int game_id, @PathVariable int mission_id){
//
//        if(isNull(gameService.findById(game_id))){
//            return ResponseEntity.notFound().build();
//        }
//
//        if ((!missionPutDTO.isHuman_visible() && !missionPutDTO.isZombie_visible()) || isNull(missionPutDTO.getName())){
//            return ResponseEntity.badRequest().build();
//        }
//
//        Mission mission = missionMapper.missionPutDTOToMission(missionPutDTO);
//        gameService.updateMission(game_id, mission_id, mission);
//
//        return ResponseEntity.noContent().build();
//    }

//    @Operation(summary = "Delete a mission of a game by ID")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "204",
//                    description = "Mission successfully deleted",
//                    content = @Content),
//            @ApiResponse(responseCode = "404",
//                    description = "Game not found with supplied ID OR this game does not include a mission of this ID",
//                    content = @Content)
//    })
//    @DeleteMapping({"{game_id}/missions/{mission_id}"})//DELETE: localhost:8080/api/v1/games/game_id/missions/mission_id
//    public ResponseEntity<MissionDTO> deleteMission(@PathVariable int game_id, @PathVariable int mission_id){
//
//        if(isNull(gameService.findById(game_id)) || !gameService.findById(game_id).getMissions().contains(missionService.findById(mission_id))){
//            return ResponseEntity.notFound().build();
//        }
//
//        gameService.deleteMissionById(mission_id);
//
//        return ResponseEntity.noContent().build();
//    }

//    @Operation(summary = "Get all kills of a game")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200",
//                    description = "Success",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = MissionDTO.class)) }),
//            @ApiResponse(responseCode = "404",
//                    description = "Did not find any kills",
//                    content = @Content)
//    })
//    @GetMapping("{game_id}/kills")//GET: localhost:8080/api/v1/games/game_id/kills
//    public ResponseEntity<Collection<KillDTO>> getAllKills(@PathVariable int game_id){
//        Collection<KillDTO> killDTOS = killMapper.killsToKillsDTO(gameService.findAllKills(game_id));//or query?
//        if(killDTOS.isEmpty())
//            return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(killDTOS);
//    }

//    @Operation(summary = "Get a kill by ID, of a specific game")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200",
//                    description = "Success",
//                    content = {@Content(mediaType = "application/json",
//                            schema = @Schema(implementation = MissionDTO.class))}),
//            @ApiResponse(responseCode = "404",
//                    description = "Game does not exist with supplied ID, OR this game does not include a mission of this ID",
//                    content = @Content)
//
//    })
//    @GetMapping("{game_id}/kills/{kill_id}")//GET: localhost:8080/api/v1/games/game_id/kills/kill_id
//    public ResponseEntity<KillDTO> getKillById(@PathVariable int game_id, @PathVariable int kill_id){
//        KillDTO killDTO = killMapper.killToKillDTO(gameService.findKillById(game_id, kill_id)); //or query?
//        if(isNull(killDTO)){
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(killDTO);
//    }

//    @Operation(summary = "Add a kill to a specific game")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "201",
//                    description = "Kill successfully added",
//                    content = @Content),
//            @ApiResponse(responseCode = "400",
//                    description = "Malformed request or wrong bite code",
//                    content = @Content),
//            @ApiResponse(responseCode = "401",
//                    description = "Bad request",
//                    content = @Content),
//            @ApiResponse(responseCode = "404",
//                    description = "Game does not exist with supplied ID",
//                    content = @Content)
//    })
//    @PostMapping("{game_id}/kills")//POST: localhost:8080/api/v1/games/game_id/kills
//    public ResponseEntity<KillDTO> addKillToGame(@RequestBody KillPostDTO killPostDTO, @PathVariable int game_id, @RequestHeader String biteCode){
//
//        if(isNull(gameService.findById(game_id))){
//            return ResponseEntity.notFound().build();
//        }
//
//        if (killPostDTO.getKiller()==0 || killPostDTO.getVictim()==0 || killPostDTO.getKiller() == killPostDTO.getVictim()){
//            return ResponseEntity.badRequest().build();
//        }
//
//        PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findByBiteCode(biteCode));
//
//        if (isNull(playerDTO)){
//            return ResponseEntity.badRequest().build();
//        }
//
//        playerService.turnHumanIntoZombie(playerDTO.getPlayer_id());
//
//        Kill kill = killMapper.killPostDTOToKill(killPostDTO);
//        killService.add(kill);
//        URI location = URI.create("api/v1/games/" + game_id + "/kills/" + kill.getKill_id());
//        return ResponseEntity.created(location).build();
//    }

//    @Operation(summary = "Updates a kill")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "204",
//                    description = "Mission successfully updated",
//                    content = @Content),
//            @ApiResponse(responseCode = "400",
//                    description = "Malformed request",
//                    content = @Content),
//            @ApiResponse(responseCode = "404",
//                    description = "Game not found with supplied ID OR this game does not include a kill of this ID",
//                    content = @Content)
//    })
//    @PutMapping({"{game_id}/kills/{kill_id}"})//PUT: localhost:8080/api/v1/games/game_id/missions/mission_id
//    public ResponseEntity<KillDTO> updateKill(@RequestBody KillPutDTO killPutDTO, @PathVariable int game_id, @PathVariable int kill_id){
//
//        if(isNull(gameService.findById(game_id))){
//            return ResponseEntity.notFound().build();
//        }
//
//        Kill kill = killMapper.killPutDTOToKill(killPutDTO);
//        killService.updateKillById(kill, kill_id);
//
//        return ResponseEntity.noContent().build();
//    }

//    @Operation(summary = "Delete a kill of a game by ID")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "204",
//                    description = "Mission successfully deleted",
//                    content = @Content),
//            @ApiResponse(responseCode = "404",
//                    description = "Game not found with supplied ID OR this game does not include a kill of this ID",
//                    content = @Content)
//    })
//    @DeleteMapping({"{game_id}/kills/{kill_id}"})//DELETE: localhost:8080/api/v1/games/game_id/missions/mission_id
//    public ResponseEntity<MissionDTO> deleteKill(@PathVariable int game_id, @PathVariable int kill_id){
//
//        if(isNull(gameService.findById(game_id))){
//            return ResponseEntity.notFound().build();
//        }
//
//        gameService.deleteKillById(kill_id);
//
//        return ResponseEntity.noContent().build();
//    }
}
