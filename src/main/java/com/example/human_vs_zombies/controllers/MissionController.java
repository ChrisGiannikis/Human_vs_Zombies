package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.mission.MissionPostDTO;
import com.example.human_vs_zombies.dto.mission.MissionPutDTO;
import com.example.human_vs_zombies.dto.player.PlayerAdminDTO;
import com.example.human_vs_zombies.dto.player.PlayerSimpleDTO;
import com.example.human_vs_zombies.mappers.MissionMapper;
import com.example.human_vs_zombies.services.mission.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/missions")
public class MissionController {

    private final MissionService missionService;
    private final MissionMapper missionMapper;

    public MissionController(MissionService missionService, MissionMapper missionMapper) {
        this.missionService = missionService;
        this.missionMapper = missionMapper;
    }

    @Operation(summary = "Gets all the missions for a game.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content( mediaType = "application/json",
                            array = @ArraySchema( schema = @Schema(implementation = PlayerAdminDTO.class)))})
    })
    @GetMapping
    public ResponseEntity findAll(){
        //check for missions that belongs to the faction of the requested user
        return ResponseEntity.ok( missionMapper.missionToMissionDTO( missionService.findAll() ) );
    }

    @Operation(summary = "Finds the mission with the given id.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content( mediaType = "application/json",
                            schema = @Schema(implementation = PlayerSimpleDTO.class))}),
            @ApiResponse( responseCode = "404",
                    description = "Mission with supplied id, does not exist! ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("{mission_id}")
    public ResponseEntity findById(@PathVariable int mission_id){
        //check for faction, if a player request a mission of another faction return 403 Forbidden
        return ResponseEntity.ok( missionMapper.missionToMissionDTO( missionService.findById(mission_id) ) );
    }

    @Operation(summary = "Creates a new mission.")
    @ApiResponses( value = {
            @ApiResponse( responseCode = "201", description = "Mission created", content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
            @ApiResponse( responseCode = "404", description = "Mission not found", content = { @Content })
    })
    @PostMapping
    public ResponseEntity createMission(@RequestBody MissionPostDTO mission) throws URISyntaxException {
        //Admin only
        missionService.add( missionMapper.missionPostDTOToMission(mission) ); //adds the given new mission
        URI uri = new URI("api/missions" + mission.getMission_id()); //creating a new uri for the new mission
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Updates the mission with the given id.")
    @ApiResponses( value = {
            @ApiResponse( responseCode = "204", description = "Mission updated", content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
            @ApiResponse( responseCode = "404", description = "Mission not found", content = { @Content })
    })
    @PutMapping("{mission_id}")
    public ResponseEntity updateMissionById(@RequestBody MissionPutDTO mission, @PathVariable int mission_id){
        //Admin only
        if(mission_id != mission.getMission_id()) //if the given id is not name as the given mission id
            return ResponseEntity.badRequest().build(); // they are different and returns bad request response
        missionService.update( missionMapper.missionPutDTOToMission(mission) ); // ids are same so call the update
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deletes the mission with the given id.")
    @ApiResponses(value = {
            @ApiResponse( responseCode =  "200",
                    description = "Mission deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerSimpleDTO.class))}),
            @ApiResponse( responseCode = "404",
                    description = "Mission with supplied id, does not exist! ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))})
    @DeleteMapping("{mission_id}")
    public ResponseEntity deleteMission(@PathVariable int mission_id){
        //Admin only
        missionService.deleteById(mission_id);
        return ResponseEntity.ok("Mission deleted successfully!");
    }
}