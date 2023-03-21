package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.game.GameDTO;
import com.example.human_vs_zombies.dto.game.GamePostDTO;
import com.example.human_vs_zombies.dto.game.GamePutDTO;
import com.example.human_vs_zombies.dto.kill.KillDTO;
import com.example.human_vs_zombies.dto.kill.KillPostDTO;
import com.example.human_vs_zombies.dto.kill.KillPutDTO;
import com.example.human_vs_zombies.dto.mission.MissionDTO;
import com.example.human_vs_zombies.dto.mission.MissionPostDTO;
import com.example.human_vs_zombies.dto.mission.MissionPutDTO;
import com.example.human_vs_zombies.dto.player.PlayerDTO;
import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.entities.Mission;
import com.example.human_vs_zombies.mappers.GameMapper;
import com.example.human_vs_zombies.mappers.KillMapper;
import com.example.human_vs_zombies.mappers.MissionMapper;
import com.example.human_vs_zombies.mappers.PlayerMapper;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.kill.KillService;
import com.example.human_vs_zombies.services.player.PlayerService;
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
    private final PlayerService playerService;
    private final KillService killService;
    private final PlayerMapper playerMapper;
    private final GameMapper gameMapper;
    private final MissionMapper missionMapper;
    private final KillMapper killMapper;

    public GameController(GameService gameService, PlayerService playerService, KillService killService, PlayerMapper playerMapper, GameMapper gameMapper, MissionMapper missionMapper, KillMapper killMapper){
        this.gameService = gameService;
        this.playerService = playerService;
        this.killService = killService;
        this.playerMapper = playerMapper;
        this.gameMapper = gameMapper;
        this.missionMapper = missionMapper;
        this.killMapper = killMapper;
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
    public ResponseEntity<Collection<GameDTO>> getAll(){
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
    public ResponseEntity<GameDTO> add(@RequestBody GamePostDTO gamePostDTO){
        if (isNull(gamePostDTO.getName()))
            return ResponseEntity.badRequest().build();
        gameService.add(gameMapper.gamePostDtoToGame(gamePostDTO));
        int game_id = gameMapper.gamePostDtoToGame(gamePostDTO).getGame_id();
        URI location = URI.create("api/v1/games/" + game_id);
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
    @PutMapping({"{id}"})//PUT: localhost:8080/api/v1/games/id
    public ResponseEntity<GameDTO> update(@RequestBody GamePutDTO gamePutDTO, @PathVariable int id){
        if(id != gamePutDTO.getGame_id()){
            return ResponseEntity.badRequest().build();
        }

        gameService.update(gameMapper.gamePutDtoToGame(gamePutDTO));

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
    @DeleteMapping({"{id}"})//DELETE: localhost:8080/api/v1/games/id
    public ResponseEntity<GameDTO> delete(@PathVariable int id){
        GameDTO gameDTO = gameMapper.gameToGameDto(gameService.findById(id));
        gameService.deleteById(gameDTO.getGame_id());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all missions of a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Did not find any missions",
                    content = @Content)
    })
    @GetMapping("{game_id}/missions")//GET: localhost:8080/api/v1/games/game_id/missions
    public ResponseEntity<Collection<MissionDTO>> getAllMissions(@PathVariable int game_id){
        Collection<MissionDTO> missionDTOS = missionMapper.missionToMissionDTO(gameService.findById(game_id).getMissions());
        if(missionDTOS.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(missionDTOS);
    }


    @Operation(summary = "Get a mission by ID, of a specific game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID, OR this game does not include a mission of this ID",
                    content = @Content)

    })
    @GetMapping("{game_id}/missions/{mission_id}")//GET: localhost:8080/api/v1/games/game_id/missions/mission_id
    public ResponseEntity<MissionDTO> getMissionById(@PathVariable int game_id, @PathVariable int mission_id){
        MissionDTO missionDTO = missionMapper.missionToMissionDTO(gameService.findMissionById(game_id, mission_id));

        if(isNull(missionDTO)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(missionDTO);
    }

    @Operation(summary = "Add a mission to a specific game")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Mission successfully added",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID",
                    content = @Content)
    })
    @PostMapping("{game_id}/missions")//POST: localhost:8080/api/v1/games/game_id/missions
    public ResponseEntity<MissionDTO> addMissionToGame(@RequestBody MissionPostDTO missionPostDTO, @PathVariable int game_id){

        if(isNull(gameService.findById(game_id))){
            return ResponseEntity.notFound().build();
        }

        if ((!missionPostDTO.isHuman_visible() && !missionPostDTO.isZombie_visible()) || isNull(missionPostDTO.getName())){
            return ResponseEntity.badRequest().build();
        }

        Mission mission = missionMapper.missionPostDTOToMission(missionPostDTO);
        gameService.addMission(game_id, mission);
        URI location = URI.create("api/v1/games/" + game_id + "/missions/" + mission.getMission_id());
        return ResponseEntity.created(location).build();
    }


    @Operation(summary = "Updates a mission")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Mission successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID OR this game does not include a mission of this ID",
                    content = @Content)
    })
    @PutMapping({"{game_id}/missions/{mission_id}"})//PUT: localhost:8080/api/v1/games/game_id/missions/mission_id
    public ResponseEntity<MissionDTO> updateMission(@RequestBody MissionPutDTO missionPutDTO, @PathVariable int game_id, @PathVariable int mission_id){

        if(isNull(gameService.findById(game_id))){
            return ResponseEntity.notFound().build();
        }

        if ((!missionPutDTO.isHuman_visible() && !missionPutDTO.isZombie_visible()) || isNull(missionPutDTO.getName())){
            return ResponseEntity.badRequest().build();
        }

        Mission mission = missionMapper.missionPutDTOToMission(missionPutDTO); //ask for put dto
        gameService.updateMission(game_id, mission_id, mission);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a mission of a game by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Mission successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID OR this game does not include a mission of this ID",
                    content = @Content)
    })
    @DeleteMapping({"{game_id}/missions/{mission_id}"})//DELETE: localhost:8080/api/v1/games/game_id/missions/mission_id
    public ResponseEntity<MissionDTO> deleteMission(@PathVariable int game_id, @PathVariable int mission_id){

        if(isNull(gameService.findById(game_id))){
            return ResponseEntity.notFound().build();
        }

        gameService.deleteMissionById(mission_id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all kills of a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Did not find any kills",
                    content = @Content)
    })
    @GetMapping("{game_id}/kills")//GET: localhost:8080/api/v1/games/game_id/kills
    public ResponseEntity<Collection<KillDTO>> getAllKills(@PathVariable int game_id){
        Collection<KillDTO> killDTOS = killMapper.killsToKillsDTO(gameService.findAllKills(game_id));//or query?
        if(killDTOS.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(killDTOS);
    }

    @Operation(summary = "Get a kill by ID, of a specific game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID, OR this game does not include a mission of this ID",
                    content = @Content)

    })
    @GetMapping("{game_id}/kills/{kill_id}")//GET: localhost:8080/api/v1/games/game_id/kills/kill_id
    public ResponseEntity<KillDTO> getKillById(@PathVariable int game_id, @PathVariable int kill_id){
        KillDTO killDTO = killMapper.killToKillDTO(gameService.findKillById(game_id, kill_id)); //or query?
        if(isNull(killDTO)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(killDTO);
    }

    @Operation(summary = "Add a kill to a specific game")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Kill successfully added",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request or wrong bite code",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID",
                    content = @Content)
    })
    @PostMapping("{game_id}/kills")//POST: localhost:8080/api/v1/games/game_id/kills
    public ResponseEntity<KillDTO> addKillToGame(@RequestBody KillPostDTO killPostDTO, @PathVariable int game_id, @RequestHeader String biteCode){

        if(isNull(gameService.findById(game_id))){
            return ResponseEntity.notFound().build();
        }

        if (killPostDTO.getKiller()==0 || killPostDTO.getVictim()==0 || killPostDTO.getKiller() == killPostDTO.getVictim()){
            return ResponseEntity.badRequest().build();
        }

        PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findByBiteCode(biteCode));

        if (isNull(playerDTO)){
            return ResponseEntity.badRequest().build();
        }

        playerService.turnHumanIntoZombie(playerDTO.getPlayer_id());

        Kill kill = killMapper.killPostDTOToKill(killPostDTO);
        killService.add(kill);
        URI location = URI.create("api/v1/games/" + game_id + "/kills/" + kill.getKill_id());
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Updates a kill")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Mission successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID OR this game does not include a kill of this ID",
                    content = @Content)
    })
    @PutMapping({"{game_id}/kills/{kill_id}"})//PUT: localhost:8080/api/v1/games/game_id/missions/mission_id
    public ResponseEntity<KillDTO> updateKill(@RequestBody KillPutDTO killPutDTO, @PathVariable int game_id, @PathVariable int kill_id){

        if(isNull(gameService.findById(game_id))){
            return ResponseEntity.notFound().build();
        }

        Kill kill = killMapper.killPutDTOToKill(killPutDTO);
        killService.updateKillById(kill, kill_id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a kill of a game by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Mission successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID OR this game does not include a kill of this ID",
                    content = @Content)
    })
    @DeleteMapping({"{game_id}/kills/{kill_id}"})//DELETE: localhost:8080/api/v1/games/game_id/missions/mission_id
    public ResponseEntity<MissionDTO> deleteKill(@PathVariable int game_id, @PathVariable int kill_id){

        if(isNull(gameService.findById(game_id))){
            return ResponseEntity.notFound().build();
        }

        gameService.deleteKillById(kill_id);

        return ResponseEntity.noContent().build();
    }
}
