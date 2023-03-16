package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.kill.KillDTO;
import com.example.human_vs_zombies.dto.kill.KillPostDTO;
import com.example.human_vs_zombies.dto.player.PlayerAdminDTO;
import com.example.human_vs_zombies.dto.player.PlayerSimpleDTO;
import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.mappers.KillMapper;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.kill.KillService;
import com.example.human_vs_zombies.services.player.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static java.util.Objects.isNull;

@RestController
@RequestMapping("api/v1")
public class KillController {
    private final KillService killService;
    private final PlayerService playerService;
    private final KillMapper killMapper;
    private final GameService gameService;



    public KillController(KillService killService, PlayerService playerService, KillMapper killMapper, GameService gameService) {
        this.killService = killService;
        this.playerService = playerService;
        this.killMapper = killMapper;
        this.gameService = gameService;
    }

    //get all kills
    @Operation(summary = "Get all the Kills of the game.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content( mediaType = "application/json",
                            array = @ArraySchema( schema = @Schema(implementation = KillDTO.class)))})
    })




    @GetMapping("/kill")
    public ResponseEntity findAll(){
        return ResponseEntity.ok(killMapper.killsToKillsDTO(killService.findAll()));
    }



    //get kill by id
    @Operation(summary = "Get a kill with the given id.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content( mediaType = "application/json",
                            schema = @Schema(implementation = KillDTO.class))}),
            @ApiResponse( responseCode = "404",
                    description = "Kill with supplied id, does not exist! ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })

    @GetMapping("/kill/{kill_id}")
    public ResponseEntity findById(@PathVariable("kill_id") int id){
        return ResponseEntity.ok(killMapper.killToKillDTO(killService.findById(id)));
    }

    // create a kill



    @Operation(summary = "Creates a kill object.")
    @ApiResponses( value = {
            @ApiResponse( responseCode = "201", description = "Kill object created", content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
            @ApiResponse( responseCode = "404", description = "Kill not found", content = { @Content })
    })
    @PostMapping("/kill")
    public ResponseEntity createKill(@RequestBody Kill kill){

        return ResponseEntity.ok(killService.add(killMapper.killToKillPostDTO(kill)));

    }


    //update a kill

    @Operation(summary = "Updates the kill object with the given id.")
    @ApiResponses( value = {
            @ApiResponse( responseCode = "204", description = "Kill updated", content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
            @ApiResponse( responseCode = "404", description = "Kill not found", content = { @Content })
    })
    @PutMapping("/kill/{kill_id}")
    public ResponseEntity updateKill(@RequestBody KillDTO killDTO, @PathVariable("kill_id") int id){
        Kill kill = killMapper.killDTOToKill(killDTO);
        KillDTO updatedDTO = killMapper.killToKillDTO(killService.update(kill));
        return ResponseEntity.ok(updatedDTO);


    }


    @Operation(summary = "Deletes the kill object with the given id.")
    @ApiResponses(value = {
            @ApiResponse( responseCode =  "200",
                    description = "Kill deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KillDTO.class))}),
            @ApiResponse( responseCode = "404",
                    description = "Player with supplied id, does not exist! ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))})
    @DeleteMapping("/kill/{kill_id}")
    public ResponseEntity deleteKillById(@PathVariable("kill_id") int id){
        if(isNull(killService.findById(id)))
            return ResponseEntity.notFound().build();


        killService.deleteById(id);
        return ResponseEntity.ok("Kill deleted successfully!");
    }



}
