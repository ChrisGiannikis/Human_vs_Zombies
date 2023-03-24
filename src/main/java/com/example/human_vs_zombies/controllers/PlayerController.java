package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.player.*;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("api/players")
public class PlayerController {

    private final PlayerService playerService;
    private final InfoController infoController;
    private final PlayerMapper playerMapper;

    public PlayerController(PlayerService playerService, InfoController infoController, PlayerMapper playerMapper) {
        this.playerService = playerService;
        this.infoController = infoController;
        this.playerMapper = playerMapper;
    }

    @Operation(summary = "Finds all the Players of the game.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success",
                            content = {@Content( mediaType = "application/json",
                                array = @ArraySchema( schema = @Schema(implementation = PlayerAdminDTO.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "Did not find any players",
                    content = @Content)
    })
    @GetMapping//GET: localhost:8080/api/players
    public ResponseEntity findAll(@AuthenticationPrincipal Jwt jwt){

        /*//if request has been from admin use the playerToPlayerAdminDTO else use the playerToPlayerSimpleDTO
        return (ResponseEntity) (is_administrator ?
                ResponseEntity.ok( playerMapper.playerToPlayerAdminDTO(playerService.findAll()) ) :
                ResponseEntity.ok( playerMapper.playerToPlayerSimpleDTO(playerService.findAll()) ));*/
             Collection<PlayerAdminDTO> playerAdminDTOS = playerMapper.playerToPlayerAdminDTO(playerService.findAll());
             if (playerAdminDTOS.isEmpty())
                 return ResponseEntity.notFound().build();
             return ResponseEntity.ok(playerAdminDTOS);

    }

    @Operation(summary = "Finds the player with the given id.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content( mediaType = "application/json",
                            schema = @Schema(implementation = PlayerDTO.class))}),
            @ApiResponse( responseCode = "404",
                    description = "Player with supplied id, does not exist! ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("{player_id}")//GET: localhost:8080/api/players/id
    public ResponseEntity findById(@PathVariable int player_id){
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
            @ApiResponse( responseCode = "404", description = "User, squad member or game not found", content = { @Content })
    })
    @PostMapping//POST: localhost:8080/api/players
    public ResponseEntity createPlayer(@RequestBody PlayerPostDTO player) throws URISyntaxException {

        playerService.add( playerMapper.playerPostDTOtoPlayer(player) ) ; //adds a new player
        int player_id = playerMapper.playerPostDTOtoPlayer(player).getPlayer_id();
        URI uri = new URI("api/players/" + player_id);  //making a new uri with the new players id
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Updates the player with the given id.")
    @ApiResponses( value = {
            @ApiResponse( responseCode = "204", description = "Player updated", content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
            @ApiResponse( responseCode = "404", description = "Player not found", content = { @Content })
    })
    @PutMapping("{player_id}")//PUT: localhost:8080/api/players/id
    public ResponseEntity updatePlayerById(@RequestBody PlayerPutDTO player, @PathVariable int player_id,@AuthenticationPrincipal Jwt jwt){
        String arrayList = jwt.getClaimAsString("roles");
        if(arrayList.contains("ADMIN")) {
            if (player_id != player.getPlayer_id())             //checking if the given id is not name as the given player id
                return ResponseEntity.badRequest().build();    //if ids are different returns bad request response
            if (isNull(playerService.findById(player_id)))   //checking if the requested mission exists
                return ResponseEntity.notFound().build();       //it is not exists so return notFound exception
            playerService.update(playerMapper.playerPutDTOtoPlayer(player)); //ids are same so call the update
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok("Not an admin");
    }

    @Operation(summary = "Deletes the player with the given id.")
    @ApiResponses(value = {
            @ApiResponse( responseCode =  "200", description = "Player deleted", content = { @Content}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
            @ApiResponse( responseCode = "404",description = "Player with supplied id, does not exist! ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))})
    @DeleteMapping("{player_id}")//DELETE: localhost:8080/api/players/id
    public ResponseEntity deletePlayerById(@PathVariable int player_id){
        playerService.deleteById(player_id);
        return ResponseEntity.ok("Player deleted successfully!");
    }
}
