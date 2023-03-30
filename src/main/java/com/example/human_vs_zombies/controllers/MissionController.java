package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.mission.MissionDTO;
import com.example.human_vs_zombies.dto.mission.MissionPostDTO;
import com.example.human_vs_zombies.dto.mission.MissionPutDTO;
import com.example.human_vs_zombies.dto.player.PlayerDTO;
import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.entities.Mission;
import com.example.human_vs_zombies.enums.State;
import com.example.human_vs_zombies.mappers.MissionMapper;
import com.example.human_vs_zombies.mappers.PlayerMapper;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.mission.MissionService;
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

import java.net.URI;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Objects.isNull;

@RestController
@CrossOrigin
@RequestMapping("api/v1/games")
public class MissionController {

    private final MissionService missionService;
    private final GameService gameService;
    private final PlayerService playerService;
    private final MissionMapper missionMapper;
    private final PlayerMapper playerMapper;
    private String roles ="";

    public MissionController(MissionService missionService, GameService gameService, PlayerService playerService, MissionMapper missionMapper, PlayerMapper playerMapper) {
        this.missionService = missionService;
        this.gameService = gameService;
        this.playerService = playerService;
        this.missionMapper = missionMapper;
        this.playerMapper = playerMapper;
    }

    @Operation(summary = "Get all missions of a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID OR did not find any missions in this game",
                    content = @Content)
    })
    @GetMapping("{game_id}/missions")//GET: localhost:8080/api/v1/games/game_id/missions
    public ResponseEntity<Collection<MissionDTO>> getAllMissions(@PathVariable int game_id, @RequestHeader int requestedByPlayerWithId, @AuthenticationPrincipal Jwt jwt){

        String arrayList = jwt.getClaimAsString("realm_access");
        if (arrayList.contains("ADMIN")) {
            Collection<MissionDTO> missionDTOS = missionMapper.missionToMissionDTO(gameService.findById(game_id).getMissions());
            if (missionDTOS.isEmpty())
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(missionDTOS);

        } else {
            PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findById(requestedByPlayerWithId));

            if (playerDTO.getGame() != game_id) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            Collection<MissionDTO> missionDTOS = missionMapper.missionToMissionDTO(gameService.findById(game_id).getMissions());

            if (playerDTO.isHuman()) {
                missionDTOS.removeIf(missionDTO -> !missionDTO.isHuman_visible());
            } else {
                missionDTOS.removeIf(missionDTO -> !missionDTO.isZombie_visible());
            }

            if (missionDTOS.isEmpty())
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(missionDTOS);
        }
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
    public ResponseEntity<MissionDTO> getMissionById(@PathVariable int game_id, @PathVariable int mission_id, @RequestHeader int requestedByPlayerWithId, @AuthenticationPrincipal Jwt jwt){

        String arrayList = jwt.getClaimAsString("realm_access");
        if (arrayList.contains("ADMIN")) {
            MissionDTO missionDTO = missionMapper.missionToMissionDTO(missionService.findById(mission_id));

            if (missionDTO.getGame() != game_id) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(missionDTO);

        } else {

            MissionDTO missionDTO = missionMapper.missionToMissionDTO(missionService.findById(mission_id));

            if (missionDTO.getGame() != game_id) {
                return ResponseEntity.notFound().build();
            }

            PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findById(requestedByPlayerWithId));

            if (playerDTO.isHuman() != missionDTO.isHuman_visible() && !playerDTO.isHuman() == !missionDTO.isZombie_visible()) {
                return ResponseEntity.badRequest().build();
            }

            if (playerDTO.getGame() != game_id) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            return ResponseEntity.ok(missionDTO);
        }
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
    public ResponseEntity<MissionPostDTO> addMissionToGame(@RequestBody MissionPostDTO missionPostDTO, @PathVariable int game_id, @AuthenticationPrincipal Jwt jwt){

        roles = jwt.getClaimAsString("realm_access");
        if(!roles.contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if ((!missionPostDTO.isHuman_visible() && !missionPostDTO.isZombie_visible()) || isNull(missionPostDTO.getName())){
            return ResponseEntity.badRequest().build();
        }

        Game game = gameService.findById(game_id);

        if(isNull(game)){
            return ResponseEntity.notFound().build();
        }

        if(game.getState() == State.COMPLETED){
            return ResponseEntity.badRequest().build();
        }
        Mission mission = missionMapper.missionPostDTOToMission(missionPostDTO);
        mission.setGame(game);
        mission.setLat(ThreadLocalRandom.current().nextDouble(game.getSe_lat(), game.getNw_lat()));
        mission.setLng(ThreadLocalRandom.current().nextDouble(game.getNw_lng(), game.getSe_lng()));
        mission = missionService.add(mission);

        URI location = URI.create("api/v1/games/" + game_id + "missions/" + mission.getMission_id());
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
    public ResponseEntity<MissionPutDTO> updateMission(@RequestBody MissionPutDTO missionPutDTO, @PathVariable int game_id, @PathVariable int mission_id, @AuthenticationPrincipal Jwt jwt){

        roles = jwt.getClaimAsString("realm_access");
        if(!roles.contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        MissionDTO missionDTO = missionMapper.missionToMissionDTO(missionService.findById(mission_id));

        if(missionDTO.getGame() != game_id){
            return ResponseEntity.notFound().build();
        }

        if ((!missionPutDTO.isHuman_visible() && !missionPutDTO.isZombie_visible()) || isNull(missionPutDTO.getName())){
            return ResponseEntity.badRequest().build();
        }

        Game game = gameService.findById(game_id);

        if(game.getState() == State.COMPLETED){
            return ResponseEntity.badRequest().build();
        }

        Mission mission = missionMapper.missionPutDTOToMission(missionPutDTO);
        mission.setMission_id(missionDTO.getMission_id());
        mission.setGame(game);
        missionService.update(mission);

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
    public ResponseEntity<MissionDTO> deleteMission(@PathVariable int game_id, @PathVariable int mission_id, @AuthenticationPrincipal Jwt jwt){

        roles = jwt.getClaimAsString("realm_access");
        if(!roles.contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        MissionDTO missionDTO = missionMapper.missionToMissionDTO(missionService.findById(mission_id));

        if(missionDTO.getGame() != game_id){
            return ResponseEntity.notFound().build();
        }

        missionService.deleteById(missionDTO.getMission_id());

        return ResponseEntity.noContent().build();
    }
}
