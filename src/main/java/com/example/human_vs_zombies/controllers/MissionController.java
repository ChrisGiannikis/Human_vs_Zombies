package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.mission.MissionDTO;
import com.example.human_vs_zombies.dto.mission.MissionPostDTO;
import com.example.human_vs_zombies.dto.mission.MissionPutDTO;
import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.entities.Mission;
import com.example.human_vs_zombies.mappers.MissionMapper;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.mission.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("api/v1/games")
public class MissionController {

    private final MissionService missionService;
    private final GameService gameService;
    private final MissionMapper missionMapper;

    public MissionController(MissionService missionService, GameService gameService, MissionMapper missionMapper) {
        this.missionService = missionService;
        this.gameService = gameService;
        this.missionMapper = missionMapper;
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

        List<MissionDTO> missionDTOS = (List<MissionDTO>)missionMapper.missionToMissionDTO(gameService.findById(game_id).getMissions());

        if(missionDTOS.isEmpty() || missionDTOS.size()<mission_id){
            return ResponseEntity.notFound().build();
        }

        missionDTOS.sort(Comparator.comparingInt(MissionDTO::getMission_id));
        MissionDTO missionDTO = missionDTOS.get(mission_id-1);

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

        Game game = gameService.findById(game_id);

        if(isNull(game)){
            return ResponseEntity.notFound().build();
        }

        if ((!missionPostDTO.isHuman_visible() && !missionPostDTO.isZombie_visible()) || isNull(missionPostDTO.getName())){
            return ResponseEntity.badRequest().build();
        }

        Mission mission = missionMapper.missionPostDTOToMission(missionPostDTO);
        mission.setGame(game);
        missionService.add(mission);

        return ResponseEntity.ok().build();
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

        Game game = gameService.findById(game_id);

        if(isNull(game)){
            return ResponseEntity.notFound().build();
        }

        if ((!missionPutDTO.isHuman_visible() && !missionPutDTO.isZombie_visible()) || isNull(missionPutDTO.getName())){
            return ResponseEntity.badRequest().build();
        }

        List<MissionDTO> missionDTOS = (List<MissionDTO>)missionMapper.missionToMissionDTO(gameService.findById(game_id).getMissions());

        if(missionDTOS.isEmpty() || missionDTOS.size()<mission_id){
            return ResponseEntity.notFound().build();
        }

        missionDTOS.sort(Comparator.comparingInt(MissionDTO::getMission_id));
        MissionDTO missionDTO = missionDTOS.get(mission_id-1);

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
    public ResponseEntity<MissionDTO> deleteMission(@PathVariable int game_id, @PathVariable int mission_id){

        if(isNull(gameService.findById(game_id))){
            return ResponseEntity.notFound().build();
        }

        List<MissionDTO> missionDTOS = (List<MissionDTO>)missionMapper.missionToMissionDTO(gameService.findById(game_id).getMissions());

        if(missionDTOS.isEmpty() || missionDTOS.size()<mission_id){
            return ResponseEntity.notFound().build();
        }

        missionDTOS.sort(Comparator.comparingInt(MissionDTO::getMission_id));
        MissionDTO missionDTO = missionDTOS.get(mission_id-1);

        missionService.deleteById(missionDTO.getMission_id());

        return ResponseEntity.noContent().build();
    }

//    @Operation(summary = "Gets all the missions for a game.")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "200", description = "Success",
//                    content = {@Content( mediaType = "application/json",
//                            array = @ArraySchema( schema = @Schema(implementation = MissionDTO.class)))}),
//            @ApiResponse(responseCode = "404",
//                    description = "Did not find any missions",
//                    content = @Content)
//    })
//    @GetMapping
//    public ResponseEntity findAll(){
//        //check for missions that belongs to the faction of the requested user
//        Collection<MissionDTO> missionDTOS = missionMapper.missionToMissionDTO( missionService.findAll());
//        if (missionDTOS.isEmpty())
//            return ResponseEntity.notFound().build();
//        return ResponseEntity.ok( missionDTOS );
//    }
//
//    @Operation(summary = "Finds the mission with the given id.")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "200", description = "Success",
//                    content = {@Content( mediaType = "application/json",
//                            schema = @Schema(implementation = MissionDTO.class))}),
//            @ApiResponse( responseCode = "404",
//                    description = "Mission with supplied id, does not exist! ",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ProblemDetail.class)))
//    })
//    @GetMapping("{mission_id}")
//    public ResponseEntity findById(@PathVariable int mission_id){
//        //check for faction, if a player request a mission of another faction return 403 Forbidden
//        return ResponseEntity.ok( missionMapper.missionToMissionDTO( missionService.findById(mission_id) ) );
//    }
//
//    @Operation(summary = "Creates a new mission.")
//    @ApiResponses( value = {
//            @ApiResponse( responseCode = "201", description = "Mission created", content = { @Content }),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
//            @ApiResponse( responseCode = "404", description = "The given Game does not exists!", content = { @Content })
//    })
//    @PostMapping
//    public ResponseEntity createMission(@RequestBody MissionPostDTO mission) throws URISyntaxException {
//        //Admin only
//        if ( isNull(mission.getName())) // if user try to create new mission without name
//            return ResponseEntity.badRequest().build(); //return bad request
//        missionService.add( missionMapper.missionPostDTOToMission(mission) ); //adds the given new mission
//        int mission_id = missionMapper.missionPostDTOToMission(mission).getMission_id();
//        URI uri = new URI("api/missions" + mission_id); //creating a new uri for the new mission
//        return ResponseEntity.created(uri).build();
//    }
//
//    @Operation(summary = "Updates the mission with the given id.")
//    @ApiResponses( value = {
//            @ApiResponse( responseCode = "204", description = "Mission updated", content = { @Content }),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
//            @ApiResponse( responseCode = "404", description = "Mission not found", content = { @Content })
//    })
//    @PutMapping("{mission_id}")
//    public ResponseEntity updateMissionById(@RequestBody MissionPutDTO mission, @PathVariable int mission_id){
//        //Admin only
//        if(mission_id != mission.getMission_id()) //if the given id is not same as the given mission id
//            return ResponseEntity.badRequest().build(); // they are different and returns bad request response
//        if ( isNull( missionService.findById(mission_id)) ) //checking if the requested mission exists
//            return ResponseEntity.notFound().build();       //it is not exists so return notFound exception
//        missionService.update( missionMapper.missionPutDTOToMission(mission) ); // ids are same so call the update
//        return ResponseEntity.noContent().build();
//    }
//
//    @Operation(summary = "Deletes the mission with the given id.")
//    @ApiResponses(value = {
//            @ApiResponse( responseCode =  "200",
//                    description = "Mission deleted",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = MissionDTO.class))}),
//            @ApiResponse( responseCode = "404",
//                    description = "Mission with supplied id, does not exist! ",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ProblemDetail.class)))})
//    @DeleteMapping("{mission_id}")
//    public ResponseEntity deleteMission(@PathVariable int mission_id){
//        //Admin only
//
//        missionService.deleteById(mission_id);
//        return ResponseEntity.ok("Mission deleted successfully!");
//    }
}
