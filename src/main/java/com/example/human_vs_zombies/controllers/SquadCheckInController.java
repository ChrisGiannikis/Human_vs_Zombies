package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.SquadCheckInDTO;
import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.entities.SquadCheckIn;
import com.example.human_vs_zombies.mappers.SquadCheckInMapper;
import com.example.human_vs_zombies.services.squadCheckIn.SquadCheckInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

@RestController
@RequestMapping("api/v1/checkIns")
public class SquadCheckInController {
    private final SquadCheckInService squadCheckInService;
    private final SquadCheckInMapper squadCheckInMapper;

    public SquadCheckInController(SquadCheckInService squadCheckInService, SquadCheckInMapper squadCheckInMapper) {
        this.squadCheckInService = squadCheckInService;
        this.squadCheckInMapper = squadCheckInMapper;
    }

    @Operation(summary = "Get all squad check-ins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Did not found any squad check-ins",
                    content = @Content)
    })
    @GetMapping//GET: localhost:8080/api/v1/checkIns
    public ResponseEntity getCheckIns(){
        //(Boolean is_administrator, SquadMember member, Player player)
        //Get a list of squad check-in markers.
        //Only administrators and members of a squad who are still in the appropriate faction may see squad check-ins
        Collection<SquadCheckInDTO> squadCheckInDTOS = squadCheckInMapper.squadCheckInToSquadCheckInDTO( squadCheckInService.findAll());
        return ResponseEntity.ok( squadCheckInDTOS);
    }

    @Operation(summary = "Create check-in")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Check-in added successfully! ",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
    })
    @PostMapping//POST: localhost:8080/api//v1/checkIns
    public ResponseEntity createCheckIn(@RequestBody SquadCheckInDTO squadCheckIn) throws URISyntaxException {
        //(SquadMember member, Player player)
        // creates a new checkin
        //Only members of a squad who are still in the appropriate faction may check-in with their squad
        squadCheckInService.add( squadCheckInMapper.squadCheckInDTOToSquadCheckIn(squadCheckIn));
        URI uri = new URI("api/v1/checkIns/" + squadCheckIn.getSquad_checkin_id());  //making a new uri with the new check-in id
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("{checkIn_id}")//DELETE: localhost:8080/api//v1/checkIns/id
    public ResponseEntity deleteCheckInById(@PathVariable int checkIn_id){
        squadCheckInService.deleteById(checkIn_id);
        return ResponseEntity.ok("Check-in deleted successfully!");
    }
}
