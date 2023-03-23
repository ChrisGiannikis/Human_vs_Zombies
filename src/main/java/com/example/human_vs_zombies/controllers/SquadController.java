package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.mission.MissionDTO;
import com.example.human_vs_zombies.dto.squad.SquadDTO;
import com.example.human_vs_zombies.dto.squad.SquadPostDTO;
import com.example.human_vs_zombies.dto.squad.SquadPutDTO;
import com.example.human_vs_zombies.entities.*;
import com.example.human_vs_zombies.enums.Rank;
import com.example.human_vs_zombies.mappers.SquadMapper;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.player.PlayerService;
import com.example.human_vs_zombies.services.squad.SquadService;
import com.example.human_vs_zombies.services.squadMember.SquadMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static java.util.Objects.isNull;

@RestController
@RequestMapping(path="api/v1/games")
public class SquadController {

    private final SquadService squadService;
    private final GameService gameService;

    private final SquadMemberService squadMemberService;
    private final PlayerService playerService;
    private final SquadMapper squadMapper;

    public SquadController(SquadService squadService, GameService gameService, SquadMemberService squadMemberService, PlayerService playerService, SquadMapper squadMapper) {
        this.squadService = squadService;
        this.gameService = gameService;
        this.squadMemberService = squadMemberService;
        this.playerService = playerService;
        this.squadMapper = squadMapper;
    }

    @Operation(summary = "Get all squads of a Game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadDTO.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID OR did not find any squads in this game",
                    content = @Content)
    })
    @GetMapping("{game_id}/squads")//GET: localhost:8080/api/v1/games/game_id/squads
    public ResponseEntity<Collection<SquadDTO>> getAllSquads(@PathVariable int game_id) {
        Collection<SquadDTO> squadDTOS = squadMapper.squadToSquadDto(gameService.findById(game_id).getSquads());
        if(squadDTOS.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(squadDTOS);
    }

    @Operation(summary = "Get a squad by ID, of a specific game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID, OR this game does not include a squad of this ID",
                    content = @Content)

    })
    @GetMapping("{game_id}/squads/{squad_id}")//GET: localhost:8080/api/v1/games/game_id/squads
    public ResponseEntity<SquadDTO> getBySquadById(@PathVariable int game_id, @PathVariable int squad_id) {
        List<SquadDTO> squadDTOS = (List<SquadDTO>)squadMapper.squadToSquadDto(gameService.findById(game_id).getSquads());

        if(squadDTOS.isEmpty() || squadDTOS.size()<squad_id){
            return ResponseEntity.notFound().build();
        }

        squadDTOS.sort(Comparator.comparingInt(SquadDTO::getSquad_id));
        SquadDTO squadDTO = squadDTOS.get(squad_id-1);
        return ResponseEntity.ok(squadDTO);
    }

    @Operation(summary = "Add a squad to a specific game")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Squad successfully added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID",
                    content = @Content)
    })
    @PostMapping("{game_id}/squads")//POST: localhost:8080/api/v1/games/game_id/squads
    public ResponseEntity<SquadDTO> addSquadToGame(@RequestBody SquadPostDTO squadPostDTO, @PathVariable int game_id, @RequestHeader int player_id) throws URISyntaxException {

        Game game = gameService.findById(game_id);
        Player player = playerService.findById(player_id);

        if(isNull(game) || isNull(player)){
            return ResponseEntity.notFound().build();
        }

        if (player.getGame().getGame_id() != game_id || !isNull(player.getSquadMember())){ //cannot be in a squad
            return ResponseEntity.badRequest().build();
        }

        Squad squad = squadService.add(squadMapper.squadPostDTOToSquad(squadPostDTO));

        SquadMember squadMember = squadMemberService.addSquadLeader(player, squad.getSquad_id());

        Set<SquadMember> squadMemberSet = new HashSet<>(Collections.emptySet());
        squadMemberSet.add(squadMember);
        squad.setSquadMembers(squadMemberSet);
        squad.setGame(game);
        squad.setHuman(player.isHuman());
        Squad updatedSquad = squadService.add(squad);

        squadMember.setSquad(updatedSquad);
        squadMemberService.update(squadMember);

        URI uri = new URI("api/v1/games/" + game_id + "squads/" + squad.getSquad_id()); //creating a new uri for the new mission
        return ResponseEntity.created(uri).build();

//        squadMember.setSquad(squad);
//        if (isNull(squadPostDTO.getName()))
//            return ResponseEntity.badRequest().build();
//        squadService.add(squadMapper.squadPostDTOToSquad(squadPostDTO));
//        int squad_id = squadMapper.squadPostDTOToSquad(squadPostDTO).getSquad_id();
//        URI location = URI.create("squads/" + squad_id);
//        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Add a squad member to a specific squad")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Squad successfully added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game OR Player OR Squad does not exist with supplied ID",
                    content = @Content)
    })
    @PostMapping("{game_id}/squads/{squad_id}/join")//POST: localhost:8080/api/v1/games/game_id/squads/squad_id/join
    public ResponseEntity<SquadDTO> addSquadMemberToSquad(@PathVariable int game_id, @PathVariable int squad_id, @RequestHeader int player_id) {

        Game game = gameService.findById(game_id);
        Player player = playerService.findById(player_id);
        Squad squad = squadService.findById(squad_id);

        if(isNull(game) || isNull(player) || isNull(squad)){
            return ResponseEntity.notFound().build();
        }

        if (player.getGame().getGame_id() != game_id || !isNull(player.getSquadMember()) || squad.getGame().getGame_id() != game_id || squad.isHuman() != player.isHuman()){ //cannot be in a squad
            return ResponseEntity.badRequest().build();
        }

        SquadMember squadMember = new SquadMember();
        squadMember.setPlayer(player);
        squadMember.setSquad(squad);
        squadMember.setRank(Rank.NOOB);
        squadMemberService.add(squadMember);

        Set<SquadMember> squadMemberSet = squad.getSquadMembers();
        squadMemberSet.add(squadMember);
        squad.setSquadMembers(squadMemberSet);
        squadService.update(squad);

        player.setSquadMember(squadMember);
        playerService.update(player);
        return ResponseEntity.ok().build();
//        squadMember.setSquad(squad);
//        if (isNull(squadPostDTO.getName()))
//            return ResponseEntity.badRequest().build();
//        squadService.add(squadMapper.squadPostDTOToSquad(squadPostDTO));
//        int squad_id = squadMapper.squadPostDTOToSquad(squadPostDTO).getSquad_id();
//        URI location = URI.create("squads/" + squad_id);
//        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Updates a squad")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Squad successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID OR this game does not include a squad of this ID",
                    content = @Content)
    })
    @PutMapping({"{game_id}/squads/{squad_id}"})//PUT: localhost:8080/api/v1/games/game_id/squads/squad_id
    public ResponseEntity<MissionDTO> updateSquad(@RequestBody SquadPutDTO squadPutDTO, @PathVariable int game_id, @PathVariable int squad_id){

        Game game = gameService.findById(game_id);
        Squad squad = squadService.findById(squad_id);

        if(isNull(game)){
            return ResponseEntity.notFound().build();
        }

        if (game_id != squad.getGame().getGame_id()){
            return ResponseEntity.notFound().build();
        }

        squad.setName(squadPutDTO.getName());
        squadService.update(squad);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a squad by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Squad successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID OR this game does not include a squad of this ID",
                    content = @Content)
    })
    @DeleteMapping({"{game_id}/squads/{squad_id}"})//DELETE: localhost:8080/api/v1/games/game_id/squads/squad_id
    public ResponseEntity<SquadDTO> deleteSquad(@PathVariable int game_id, @PathVariable int squad_id) {

        if(isNull(gameService.findById(game_id))){
            return ResponseEntity.notFound().build();
        }

        if(game_id != squadService.findById(squad_id).getGame().getGame_id()) {
            return ResponseEntity.notFound().build();
        }

        squadService.deleteById(squad_id);
        return ResponseEntity.noContent().build();

    }

//
//    @Operation(summary = "Updates a squad")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "204",
//                    description = "Squad successfully updated",
//                    content = {@Content(mediaType = "application/json",
//                            schema = @Schema(implementation = SquadDTO.class))}),
//            @ApiResponse(responseCode = "400",
//                    description = "Malformed request",
//                    content = @Content),
//            @ApiResponse(responseCode = "404",
//                    description = "Squad not found with supplied ID",
//                    content = @Content)
//    })
//    @PutMapping({"{id}"})//PUT: localhost:8080/api/v1/squads/id
//    public ResponseEntity<SquadDTO> update(@RequestBody SquadPutDTO squadPutDTO, @PathVariable int id) {
//        if (id != squadPutDTO.getSquad_id()) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        squadService.update(squadMapper.squadPutDTOToSquad(squadPutDTO));
//
//        return ResponseEntity.noContent().build();
//    }
//
}
