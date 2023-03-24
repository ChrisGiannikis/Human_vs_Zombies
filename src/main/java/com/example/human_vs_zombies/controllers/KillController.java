package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.kill.KillDTO;
import com.example.human_vs_zombies.dto.kill.KillPostDTO;
import com.example.human_vs_zombies.dto.kill.KillPutDTO;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

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
                            array = @ArraySchema( schema = @Schema(implementation = KillDTO.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "Did not find any kills",
                    content = @Content)
    })
    @GetMapping("/kills")
    public ResponseEntity<Collection<KillDTO>> findAll(){
        Collection<KillDTO> killDTOS = killMapper.killsToKillsDTO(killService.findAll());
        if(killDTOS.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(killDTOS);
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
    @GetMapping("/kills/{kill_id}")
    public ResponseEntity findById(@PathVariable("kill_id") int id){
        return ResponseEntity.ok(killMapper.killToKillDTO(killService.findById(id)));
    }

    // create a kill
    @Operation(summary = "Creates a kill object.")
    @ApiResponses( value = {
            @ApiResponse( responseCode = "201", description = "Kill object created", content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
            @ApiResponse( responseCode = "404", description = "Killer or victim does not exist! ", content = { @Content })
    })
    @PostMapping("/kills")
    public ResponseEntity createKill(@RequestBody KillPostDTO killPostDTO,@AuthenticationPrincipal Jwt jwt){
        String arrayList = jwt.getClaimAsString("roles");
        if(arrayList.contains("ADMIN")) {
            if (killPostDTO.getLng() == 0 || killPostDTO.getLat() == 0)
                return ResponseEntity.badRequest().build();
            killService.add(killMapper.killPostDTOToKill(killPostDTO));
            int kill_id = killMapper.killPostDTOToKill(killPostDTO).getKill_id();
            URI location = URI.create("/" + kill_id);
            return ResponseEntity.created(location).build();
        }
        //throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return ResponseEntity.badRequest().build();

    }


    //update a kill

    @Operation(summary = "Updates the kill object with the given id.")
    @ApiResponses( value = {
            @ApiResponse( responseCode = "200", description = "Kill updated", content = { @Content }),
            @ApiResponse( responseCode = "204", description = "Kill updated", content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
            @ApiResponse( responseCode = "404", description = "Kill not found", content = { @Content })
    })
    @PutMapping("/kill/{kill_id}")
    public ResponseEntity updateKill(@RequestBody KillPutDTO killPutDTO, @PathVariable("kill_id") int id, @AuthenticationPrincipal Jwt jwt){
        //killer and admin
        String arrayList = jwt.getClaimAsString("roles");
        if(arrayList.contains("ADMIN")) {
            if(killPutDTO.getKill_id() != id)
                return ResponseEntity.badRequest().build();
            Kill kill = killMapper.killPutDTOToKill(killPutDTO);
            killService.updateKillById(kill,id);
            return ResponseEntity.noContent().build();
        }
        //throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return ResponseEntity.badRequest().build();
    }


    @Operation(summary = "Deletes the kill object with the given id.")
    @ApiResponses(value = {
            @ApiResponse( responseCode =  "204",
                    description = "Kill deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KillDTO.class))}),
            @ApiResponse( responseCode = "404",
                    description = "Kill with supplied id, does not exist! ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))})
    @DeleteMapping("/kill/{kill_id}")
    public ResponseEntity<KillDTO> deleteKillById(@PathVariable("kill_id") int id,@AuthenticationPrincipal Jwt jwt){
        String arrayList = jwt.getClaimAsString("roles");
        if(arrayList.contains("ADMIN")) {
            KillDTO killDTO = killMapper.killToKillDTO(killService.findById(id));
            killService.deleteById(killDTO.getKill_id());
            return ResponseEntity.noContent().build();
        }
        //throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return ResponseEntity.badRequest().build();

    }



}
