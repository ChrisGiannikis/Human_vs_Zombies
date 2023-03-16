package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.player.PlayerAdminDTO;
import com.example.human_vs_zombies.dto.player.PlayerPostDTO;
import com.example.human_vs_zombies.dto.player.PlayerPutDTO;
import com.example.human_vs_zombies.dto.player.PlayerSimpleDTO;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.mappers.PlayerMapper;
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
import java.net.URI;
import java.net.URISyntaxException;
import static java.util.Objects.isNull;

@RestController
@RequestMapping("api/players")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    public PlayerController(PlayerService playerService, PlayerMapper playerMapper) {
        this.playerService = playerService;
        this.playerMapper = playerMapper;
    }

    @Operation(summary = "Finds all the Players of the game.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success",
                            content = {@Content( mediaType = "application/json",
                                array = @ArraySchema( schema = @Schema(implementation = PlayerAdminDTO.class)))})
    })
    @GetMapping
    public ResponseEntity findAll(Boolean is_administrator){
        /*//if request has been from admin use the playerToPlayerAdminDTO else use the playerToPlayerSimpleDTO
        return (ResponseEntity) (is_administrator ?
                ResponseEntity.ok( playerMapper.playerToPlayerAdminDTO(playerService.findAll()) ) :
                ResponseEntity.ok( playerMapper.playerToPlayerSimpleDTO(playerService.findAll()) ));*/
        return ResponseEntity.ok( playerMapper.playerToPlayerAdminDTO( playerService.findAll()));
    }

    @Operation(summary = "Finds the player with the given id.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content( mediaType = "application/json",
                            schema = @Schema(implementation = PlayerSimpleDTO.class))}),
            @ApiResponse( responseCode = "404",
                    description = "Player with supplied id, does not exist! ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("{player_id}")
    public ResponseEntity findById(@PathVariable int player_id, Boolean is_administrator){
       /* //if request has been from admin use the playerToPlayerAdminDTO else use the playerToPlayerSimpleDTO
        return (ResponseEntity) (is_administrator ?
                ResponseEntity.ok( playerMapper.playerToPlayerAdminDTO( playerService.findById(id)) ) :
                ResponseEntity.ok( playerMapper.playerToPlayerSimpleDTO(playerService.findById(id)) ));*/
        return ResponseEntity.ok(playerMapper.playerToPlayerAdminDTO( playerService.findById(player_id) ));
    }

    @Operation(summary = "Creates a new player.")
    @ApiResponses( value = {
            @ApiResponse( responseCode = "201", description = "Player created", content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
            @ApiResponse( responseCode = "404", description = "Player not found", content = { @Content })
    })
    @PostMapping
    public ResponseEntity createPlayer(@RequestBody PlayerPostDTO player) throws URISyntaxException {
        playerService.add( playerMapper.playerPostDTOtoPlayer(player) ) ; //adds a new player
        URI uri = new URI("api/players/" + player.getPlayer_id());  //making a new uri with the new players id
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Updates the player with the given id.")
    @ApiResponses( value = {
            @ApiResponse( responseCode = "204", description = "Player updated", content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
            @ApiResponse( responseCode = "404", description = "Player not found", content = { @Content })
    })
    @PutMapping("{player_id}")
    public ResponseEntity updatePlayerById(@RequestBody PlayerPutDTO player, @PathVariable int player_id){
        if (player_id != player.getPlayer_id())  //checking if the given id is not name as the given player id
            return  ResponseEntity.badRequest().build();  //if ids are different returns bad request response
        playerService.update( playerMapper.playerPutDTOtoPlayer(player) ); //ids are same so call the update
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deletes the player with the given id.")
    @ApiResponses(value = {
            @ApiResponse( responseCode =  "200",
                    description = "Player deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerSimpleDTO.class))}),
            @ApiResponse( responseCode = "404",
                    description = "Player with supplied id, does not exist! ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))})
    @DeleteMapping("{player_id}")
    public ResponseEntity deletePlayerById(@PathVariable int player_id){
        //check for foreign keys
        //firstly check if player exists
        if ( isNull( playerService.findById(player_id)) )
            return ResponseEntity.notFound().build();
        playerService.deleteById(player_id);
        return ResponseEntity.ok("Player deleted successfully!");
    }
}
