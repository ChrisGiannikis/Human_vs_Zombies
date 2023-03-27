package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.squadCheckIn.SquadCheckInDTO;
import com.example.human_vs_zombies.dto.squadCheckIn.SquadCheckInPostDTO;
import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.entities.Squad;
import com.example.human_vs_zombies.entities.SquadCheckIn;
import com.example.human_vs_zombies.entities.SquadMember;
import com.example.human_vs_zombies.enums.State;
import com.example.human_vs_zombies.mappers.SquadCheckInMapper;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.player.PlayerService;
import com.example.human_vs_zombies.services.squad.SquadService;
import com.example.human_vs_zombies.services.squadCheckIn.SquadCheckInService;
import com.example.human_vs_zombies.services.squadMember.SquadMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("api/v1/games")
public class SquadCheckInController {
    private final SquadCheckInService squadCheckInService;
    private final SquadService squadService;
    private final GameService gameService;
    private final SquadMemberService squadMemberService;
    private final PlayerService playerService;
    private final SquadCheckInMapper squadCheckInMapper;

    public SquadCheckInController(SquadCheckInService squadCheckInService, SquadService squadService, GameService gameService, SquadMemberService squadMemberService, PlayerService playerService, SquadCheckInMapper squadCheckInMapper) {
        this.squadCheckInService = squadCheckInService;
        this.squadService = squadService;
        this.gameService = gameService;
        this.squadMemberService = squadMemberService;
        this.playerService = playerService;
        this.squadCheckInMapper = squadCheckInMapper;
    }

    @Operation(summary = "Get all squad check-ins of a squad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadCheckInDTO.class)) }),
            @ApiResponse(responseCode = "400",
                    description = "This player cannot see this squad's Check-Ins",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game OR Squad OR SquadCheckIn does not exist with supplied ID",
                    content = @Content)
    })
    @GetMapping("{game_id}/squads/{squad_id}/check-ins")//GET: localhost:8080/api/v1/games/game_id/squads/squad_id/check-ins
    public ResponseEntity<Collection<SquadCheckInDTO>> getCheckIns(@PathVariable int game_id, @RequestHeader int player_id, @PathVariable int squad_id){

        Squad squad = squadService.findById(squad_id);

        if(squad.getGame().getGame_id()!=game_id){
            return ResponseEntity.notFound().build();
        }

        Collection<SquadMember> squadMembers = squad.getSquadMembers();

        if(!squadMembers.contains(playerService.findById(player_id).getSquadMember())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Collection<SquadCheckIn> squadCheckIns = new java.util.ArrayList<>(Collections.emptySet());

        for(SquadMember s: squadMembers){
            squadCheckIns.addAll(s.getSquadCheckIns());
        }

        Collection<SquadCheckInDTO> squadCheckInDTOS = squadCheckInMapper.squadCheckInToSquadCheckInDTO( squadCheckIns);
        if (squadCheckInDTOS.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok( squadCheckInDTOS);
    }

    @Operation(summary = "Create check-in")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Check-in added successfully! ", content = @Content),
            @ApiResponse(responseCode = "400", description = "This player cannot create check-in in this squad", content = @Content),
            @ApiResponse( responseCode = "404", description = "The given Squad Member does not exists!", content = { @Content })
    })
    @PostMapping("{game_id}/squads/{squad_id}/check-ins")//POST: localhost:8080/api//v1/checkIns
    public ResponseEntity<SquadCheckInPostDTO> createCheckIn(@RequestBody SquadCheckInPostDTO squadCheckInPostDTO, @PathVariable int game_id, @PathVariable int squad_id, @RequestHeader int squadMember_id) {

        SquadMember squadMember = squadMemberService.findById(squadMember_id);
        Game game = gameService.findById(game_id);

        if(squadMember.getSquad().getGame().getGame_id()!=game_id){
            return ResponseEntity.notFound().build();
        }

        if(gameService.findById(game_id).getState()!= State.IN_PROGRESS){
            return ResponseEntity.badRequest().build();
        }

        if(squadMember.getSquad().getSquad_id()!=squad_id){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        SquadCheckIn squadCheckIn = squadCheckInMapper.squadCheckInPostDTOToSquadCheckIn(squadCheckInPostDTO);
        squadCheckIn.setLat(ThreadLocalRandom.current().nextDouble(game.getSe_lat(), game.getNw_lat()));
        squadCheckIn.setLng(ThreadLocalRandom.current().nextDouble(game.getNw_lng(), game.getSe_lng()));
        squadCheckIn.setSquadMember(squadMember);

        squadCheckInService.add( squadCheckIn);

        return ResponseEntity.ok().build();
//        int squadCheckIn_id = squadCheckInMapper.squadCheckInPostDTOToSquadCheckIn(squadCheckIn).getSquad_checkin_id();
//        URI uri = new URI("api/v1/checkIns/" + squadCheckIn_id);  //making a new uri with the new check-in id
//        return ResponseEntity.created(uri).build();
    }
//
//    @DeleteMapping("{checkIn_id}")//DELETE: localhost:8080/api//v1/checkIns/id
//    public ResponseEntity deleteCheckInById(@PathVariable int checkIn_id){
//        squadCheckInService.deleteById(checkIn_id);
//        return ResponseEntity.ok("Check-in deleted successfully!");
//    }
}
