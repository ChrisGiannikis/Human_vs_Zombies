package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.game.GameDTO;
import com.example.human_vs_zombies.dto.kill.KillDTO;
import com.example.human_vs_zombies.dto.kill.KillPostDTO;
import com.example.human_vs_zombies.dto.kill.KillPutDTO;
import com.example.human_vs_zombies.dto.mission.MissionDTO;
import com.example.human_vs_zombies.dto.player.PlayerDTO;
import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.enums.State;
import com.example.human_vs_zombies.mappers.GameMapper;
import com.example.human_vs_zombies.mappers.KillMapper;
import com.example.human_vs_zombies.mappers.PlayerMapper;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.kill.KillService;
import com.example.human_vs_zombies.services.player.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Collection;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("api/v1/games")
public class KillController {
    private final KillService killService;
    private final PlayerService playerService;
    private final GameService gameService;
    private final KillMapper killMapper;
    private final PlayerMapper playerMapper;

    private final GameMapper gameMapper;




    public KillController(KillService killService, PlayerService playerService, KillMapper killMapper, PlayerMapper playerMapper, GameService gameService, GameMapper gameMapper) {
        this.killService = killService;
        this.playerService = playerService;
        this.killMapper = killMapper;
        this.playerMapper = playerMapper;
        this.gameService = gameService;
        this.gameMapper = gameMapper;
    }

    @Operation(summary = "Get all kills of a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KillDTO.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID, OR did not find any kills in this game",
                    content = @Content)
    })
    @GetMapping("{game_id}/kills")//GET: localhost:8080/api/v1/games/game_id/kills
    public ResponseEntity<Collection<KillDTO>> getAllKills(@PathVariable int game_id, @RequestHeader int requestedByPlayerWithId){

        PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findById(requestedByPlayerWithId));

        if(playerDTO.getGame() != game_id){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Collection<KillDTO> killDTOS = killMapper.killsToKillsDTO(killService.findAllKillsByGameId(game_id));
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
    public ResponseEntity<KillDTO> getKillById(@PathVariable int game_id, @PathVariable int kill_id, @RequestHeader int requestedByPlayerWithId){

        PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findById(requestedByPlayerWithId));

        if(playerDTO.getGame()!=game_id){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

//        if(isNull(gameService.findById(game_id))){
//            return ResponseEntity.notFound().build();
//        }
//
//        List<KillDTO> killDTOS = (List<KillDTO>)killMapper.killsToKillsDTO(killService.findAllKillsByGameId(game_id));
//
//        if(killDTOS.isEmpty() || killDTOS.size()<kill_id){
//            return ResponseEntity.notFound().build();
//        }
//
//        killDTOS.sort(Comparator.comparingInt(KillDTO::getKill_id));

        KillDTO killDTO = killMapper.killToKillDTO(killService.findKillByKillIdAndGameId(game_id, kill_id));
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

        GameDTO gameDTO = gameMapper.gameToGameDto(gameService.findById(game_id));

        if(isNull(gameDTO)){
            return ResponseEntity.notFound().build();
        }

        if (killPostDTO.getKiller()==0 || gameDTO.getState()!= State.IN_PROGRESS){
            return ResponseEntity.badRequest().build();
        }

        PlayerDTO victim = playerMapper.playerToPlayerSimpleDTO(playerService.findByBiteCode(biteCode));
        PlayerDTO killer = playerMapper.playerToPlayerSimpleDTO(playerService.findById(killPostDTO.getKiller()));

        if (isNull(victim) || isNull(killer)){
            return ResponseEntity.badRequest().build();
        }

        if(victim.getGame()!= game_id || killer.getGame()!=game_id || killer.getPlayer_id() == victim.getPlayer_id()){ //cannot kill himself, checks if killer and victim are in same game
            return ResponseEntity.badRequest().build();
        }

        if(!victim.isHuman() || killer.isHuman()){ //humans cannot kill, zombies cannot get killed
            return ResponseEntity.badRequest().build();
        }

        playerService.turnHumanIntoZombie(victim.getPlayer_id());

        Kill kill = killMapper.killPostDTOToKill(killPostDTO);
        kill.setVictim(playerService.findByBiteCode(biteCode));
        kill = killService.add(kill);

        URI location = URI.create("api/v1/games/" + game_id + "kills/" + kill.getKill_id());
        return ResponseEntity.created(location).build();

//        return ResponseEntity.ok().build();
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
    public ResponseEntity<KillDTO> updateKill(@RequestBody KillPutDTO killPutDTO, @PathVariable int game_id, @PathVariable int kill_id, @RequestHeader int playerWhoWantsToUpdate){

        //-------------------------------------ONLY ADMIN OR KILLER CAN UPDATE A KILL----------------------------------------------------

        KillDTO killDTO = killMapper.killToKillDTO(killService.findById(kill_id));
        PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findById(playerWhoWantsToUpdate));

        if(killDTO.getKiller()!=playerDTO.getPlayer_id()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if(game_id!=playerDTO.getGame()){
            return ResponseEntity.notFound().build();
        }

        if(gameService.findById(game_id).getState() != State.IN_PROGRESS){
            return ResponseEntity.badRequest().build();
        }

//        List<KillDTO> killDTOS = (List<KillDTO>)killMapper.killsToKillsDTO(killService.findAllKillsByGameId(game_id));
//
//        if(killDTOS.isEmpty() || killDTOS.size()<kill_id){
//            return ResponseEntity.notFound().build();
//        }
//
//        killDTOS.sort(Comparator.comparingInt(KillDTO::getKill_id));
//        KillDTO killDTO = killMapper.killToKillDTO(killService.findKillByKillIdAndGameId(game_id, killDTOS.get(kill_id-1).getKill_id()));

        Kill kill = killMapper.killPutDTOToKill(killPutDTO);
        kill.setKill_id(kill_id);
        killService.update(kill);
//        killService.updateKillById(kill, killService.findKillByKillIdAndGameId(game_id, killDTO.getKill_id()).getKill_id());

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a kill of a game by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Kill successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID OR this game does not include a kill of this ID",
                    content = @Content)
    })
    @DeleteMapping({"{game_id}/kills/{kill_id}"})//DELETE: localhost:8080/api/v1/games/game_id/missions/mission_id
    public ResponseEntity<KillDTO> deleteKill(@PathVariable int game_id, @PathVariable int kill_id){

        //-------------------------------------ADMIN ONLY---------------------------------------------------------------------------

        if(game_id != killService.findById(kill_id).getVictim().getGame().getGame_id()){
            return ResponseEntity.notFound().build();
        }

        killService.deleteById(kill_id);

        return ResponseEntity.noContent().build();
    }

    //get all kills
//    @Operation(summary = "Get all the Kills of the game.")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "200", description = "Success",
//                    content = {@Content( mediaType = "application/json",
//                            array = @ArraySchema( schema = @Schema(implementation = KillDTO.class)))}),
//            @ApiResponse(responseCode = "404",
//                    description = "Did not find any kills",
//                    content = @Content)
//    })
//    @GetMapping("/kills")
//    public ResponseEntity<Collection<KillDTO>> findAll(){
//        Collection<KillDTO> killDTOS = killMapper.killsToKillsDTO(killService.findAll());
//        if(killDTOS.isEmpty())
//            return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(killDTOS);
//    }
//
//
//
//    //get kill by id
//    @Operation(summary = "Get a kill with the given id.")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "200", description = "Success",
//                    content = {@Content( mediaType = "application/json",
//                            schema = @Schema(implementation = KillDTO.class))}),
//            @ApiResponse( responseCode = "404",
//                    description = "Kill with supplied id, does not exist! ",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ProblemDetail.class)))
//    })
//    @GetMapping("/kills/{kill_id}")
//    public ResponseEntity findById(@PathVariable("kill_id") int id){
//        return ResponseEntity.ok(killMapper.killToKillDTO(killService.findById(id)));
//    }
//
//    // create a kill
//    @Operation(summary = "Creates a kill object.")
//    @ApiResponses( value = {
//            @ApiResponse( responseCode = "201", description = "Kill object created", content = { @Content }),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
//            @ApiResponse( responseCode = "404", description = "Killer or victim does not exist! ", content = { @Content })
//    })
//    @PostMapping("/kills")
//    public ResponseEntity createKill(@RequestBody KillPostDTO killPostDTO){
//        if (killPostDTO.getLng() == 0 || killPostDTO.getLat() == 0 )
//            return ResponseEntity.badRequest().build();
//        killService.add(killMapper.killPostDTOToKill(killPostDTO));
//        int kill_id = killMapper.killPostDTOToKill(killPostDTO).getKill_id();
//        URI location = URI.create("/" + kill_id);
//        return ResponseEntity.created(location).build();
//
//    }
//
//
//    //update a kill
//
//    @Operation(summary = "Updates the kill object with the given id.")
//    @ApiResponses( value = {
//            @ApiResponse( responseCode = "200", description = "Kill updated", content = { @Content }),
//            @ApiResponse( responseCode = "204", description = "Kill updated", content = { @Content }),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
//            @ApiResponse( responseCode = "404", description = "Kill not found", content = { @Content })
//    })
//    @PutMapping("/kill/{kill_id}")
//    public ResponseEntity updateKill(@RequestBody KillPutDTO killPutDTO, @PathVariable("kill_id") int id){
//        if(killPutDTO.getKill_id() != id)
//            return ResponseEntity.badRequest().build();
//        Kill kill = killMapper.killPutDTOToKill(killPutDTO);
//        killService.updateKillById(kill,id);
//        return ResponseEntity.noContent().build();
//    }
//
//
//    @Operation(summary = "Deletes the kill object with the given id.")
//    @ApiResponses(value = {
//            @ApiResponse( responseCode =  "204",
//                    description = "Kill deleted",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = KillDTO.class))}),
//            @ApiResponse( responseCode = "404",
//                    description = "Kill with supplied id, does not exist! ",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ProblemDetail.class)))})
//    @DeleteMapping("/kill/{kill_id}")
//    public ResponseEntity<KillDTO> deleteKillById(@PathVariable("kill_id") int id){
//        KillDTO killDTO = killMapper.killToKillDTO(killService.findById(id));
//        killService.deleteById(killDTO.getKill_id());
//        return ResponseEntity.noContent().build();
//    }



}
