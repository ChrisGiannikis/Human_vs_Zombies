package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.player.*;
import com.example.human_vs_zombies.dto.user.UserDTO;
import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.enums.State;
import com.example.human_vs_zombies.mappers.PlayerMapper;
import com.example.human_vs_zombies.mappers.UserMapper;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.player.PlayerService;
import com.example.human_vs_zombies.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("api/v1/games")
public class PlayerController {

    private final PlayerService playerService;

    private final UserService userService;
    private final GameService gameService;
    private final PlayerMapper playerMapper;
    private final UserMapper userMapper;
    private String roles ="";

    public PlayerController(PlayerService playerService, UserService userService, GameService gameService, PlayerMapper playerMapper, UserMapper userMapper) {
        this.playerService = playerService;
        this.userService = userService;
        this.gameService = gameService;
        this.playerMapper = playerMapper;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Get all players of a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerDTO.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID OR did not find any players in this game",
                    content = @Content)
    })
    @GetMapping("{game_id}/players")//GET: localhost:8080/api/v1/games/game_id/players
    public ResponseEntity<Collection<PlayerDTO>> getAllPlayers(@PathVariable int game_id, @RequestHeader int requestedByPlayerWithId, @AuthenticationPrincipal Jwt jwt){

        //------------------ONLY ADMINS CAN SEE IF PLAYER IS PATIENT ZERO--------------------------------------------------
        String arrayList = jwt.getClaimAsString("roles");
        if(arrayList.contains("ADMIN")) {
            //------------------------------------------
        }

        PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findById(requestedByPlayerWithId));

        if(playerDTO.getGame() != game_id){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Collection<PlayerDTO> playerDTOS = playerMapper.playerToPlayerSimpleDTO(gameService.findById(game_id).getPlayers());
        if(playerDTOS.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(playerDTOS);
    }

    @Operation(summary = "Get a player by ID, of a specific game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID, OR this game does not include a player of this ID",
                    content = @Content)

    })
    @GetMapping("{game_id}/players/{player_id}")//GET: localhost:8080/api/v1/games/game_id/players/player_id
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable int game_id, @PathVariable int player_id, @RequestHeader int requestedByPlayerWithId, @AuthenticationPrincipal Jwt jwt){

        //------------------ONLY ADMINS CAN SEE IF PLAYER IS PATIENT ZERO--------------------------------------------------
        String arrayList = jwt.getClaimAsString("roles");
        if(arrayList.contains("ADMIN")) {
            //------------------------------------------
        }


        PlayerDTO requestedByPlayerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findById(requestedByPlayerWithId));

        if(requestedByPlayerDTO.getGame() != game_id){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findById(player_id));

        if(playerDTO.getGame() != game_id){
            return ResponseEntity.notFound().build();
        }
//
//        List<PlayerDTO> playerDTOS = (List<PlayerDTO>)playerMapper.playerToPlayerSimpleDTO(gameService.findById(game_id).getPlayers());
//
//        if(playerDTOS.isEmpty() || playerDTOS.size()<player_id){
//            return ResponseEntity.notFound().build();
//        }
//
//        playerDTOS.sort(Comparator.comparingInt(PlayerDTO::getPlayer_id));
//        PlayerDTO playerDTO = playerDTOS.get(player_id-1);

        return ResponseEntity.ok(playerDTO);
    }

    @Operation(summary = "Add a player to a specific game")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Player successfully added",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game does not exist with supplied ID OR User does not exist with supplied ID",
                    content = @Content)
    })
    @PostMapping("{game_id}/players")//POST: localhost:8080/api/v1/games/game_id/players
    public ResponseEntity<PlayerDTO> addPlayerToGame(@RequestBody PlayerPostDTO playerPostDTO, @PathVariable int game_id, @AuthenticationPrincipal Jwt jwt){

        //-------------------ONLY ADMIN CAN CREATE A PLAYER WITH IS_HUMAN AND IS_PATIENT_ZERO ATTRIBUTES------------------
        //-------------------PLAYERS WHO REGISTER THEMSELVES GET DEFAULT VALUES-------------------------------------------

        Game game = gameService.findById(game_id);

        if(isNull(game)){
            return ResponseEntity.notFound().build();
        }

        if(game.getState()!=State.REGISTRATION){
            return ResponseEntity.badRequest().build();
        }

        UserDTO userDTO = userMapper.UserToUserDTO(userService.findById(playerPostDTO.getUser()));

        if ((playerPostDTO.isHuman() && playerPostDTO.isPatient_zero()) || userDTO.getPlayer()!=0){
            return ResponseEntity.badRequest().build();
        }

        Player player = new Player();
        player.setGame(game);
        player.setBiteCode(RandomStringUtils.randomAlphanumeric(20).toUpperCase());
        String arrayList = jwt.getClaimAsString("roles");
        if(arrayList.contains("ADMIN")) {
            player = playerMapper.playerPostDTOtoPlayer(playerPostDTO);
        }
        playerService.add(player);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update player: Puts player to opponent team when game is on Registration State")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Player successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Game State: Completed or In Progress",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID OR this game does not include a player of this ID",
                    content = @Content)
    })
    @PutMapping({"{game_id}/players/{player_id}"})//PUT: localhost:8080/api/v1/games/game_id/players/player_id
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable int game_id, @PathVariable int player_id, @AuthenticationPrincipal Jwt jwt){

        //-----------------------------------------ADMIN ONLY-------------------------------------------------------------------------
        roles = jwt.getClaimAsString("roles");
        if(!roles.contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Game game = gameService.findById(game_id);

        if(isNull(game)){
            return ResponseEntity.notFound().build();
        }

        if(game.getState() != State.REGISTRATION){
            return ResponseEntity.badRequest().build();
        }

//        List<PlayerDTO> playerDTOS = (List<PlayerDTO>)playerMapper.playerToPlayerSimpleDTO(gameService.findById(game_id).getPlayers());
//
//        if(playerDTOS.isEmpty() || playerDTOS.size()<player_id){
//            return ResponseEntity.notFound().build();
//        }
//
//        playerDTOS.sort(Comparator.comparingInt(PlayerDTO::getPlayer_id));
//        PlayerDTO playerDTO = playerDTOS.get(player_id-1);

        playerService.changeTeams(player_id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a player of a game by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Player successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Game not found with supplied ID OR this game does not include a player of this ID",
                    content = @Content)
    })
    @DeleteMapping({"{game_id}/players/{player_id}"})//DELETE: localhost:8080/api/v1/games/game_id/players/player_id
    public ResponseEntity<PlayerDTO> deletePlayer(@PathVariable int game_id, @PathVariable int player_id, @AuthenticationPrincipal Jwt jwt){

        //---------------ADMIN ONLY------------------------------------------------------------------------------------------
        roles = jwt.getClaimAsString("roles");
        if(!roles.contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO(playerService.findById(player_id));

        if(playerDTO.getGame() != game_id){
            return ResponseEntity.notFound().build();
        }

//        List<PlayerDTO> playerDTOS = (List<PlayerDTO>)playerMapper.playerToPlayerSimpleDTO(gameService.findById(game_id).getPlayers());
//
//        if(playerDTOS.isEmpty() || playerDTOS.size()<player_id){
//            return ResponseEntity.notFound().build();
//        }
//
//        playerDTOS.sort(Comparator.comparingInt(PlayerDTO::getPlayer_id));
//        PlayerDTO playerDTO = playerDTOS.get(player_id-1);

        playerService.deleteById(player_id);

        return ResponseEntity.noContent().build();
    }

//    @Operation(summary = "Finds all the Players of the game.")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "200", description = "Success",
//                            content = {@Content( mediaType = "application/json",
//                                array = @ArraySchema( schema = @Schema(implementation = PlayerAdminDTO.class)))}),
//            @ApiResponse(responseCode = "404",
//                    description = "Did not find any players",
//                    content = @Content)
//    })
//    @GetMapping//GET: localhost:8080/api/players
//    public ResponseEntity findAll(){
//        /*//if request has been from admin use the playerToPlayerAdminDTO else use the playerToPlayerSimpleDTO
//        return (ResponseEntity) (is_administrator ?
//                ResponseEntity.ok( playerMapper.playerToPlayerAdminDTO(playerService.findAll()) ) :
//                ResponseEntity.ok( playerMapper.playerToPlayerSimpleDTO(playerService.findAll()) ));*/
//        Collection<PlayerAdminDTO> playerAdminDTOS = playerMapper.playerToPlayerAdminDTO(playerService.findAll());
//        if(playerAdminDTOS.isEmpty())
//            return ResponseEntity.notFound().build();
//        return ResponseEntity.ok( playerAdminDTOS);
//    }
//
//    @Operation(summary = "Finds the player with the given id.")
//    @ApiResponses( value = {
//            @ApiResponse(responseCode = "200", description = "Success",
//                    content = {@Content( mediaType = "application/json",
//                            schema = @Schema(implementation = PlayerDTO.class))}),
//            @ApiResponse( responseCode = "404",
//                    description = "Player with supplied id, does not exist! ",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ProblemDetail.class)))
//    })
//    @GetMapping("{player_id}")//GET: localhost:8080/api/players/id
//    public ResponseEntity findById(@PathVariable int player_id, Boolean is_administrator){
//       /* //if request has been from admin use the playerToPlayerAdminDTO else use the playerToPlayerSimpleDTO
//        return (ResponseEntity) (is_administrator ?
//                ResponseEntity.ok( playerMapper.playerToPlayerAdminDTO( playerService.findById(id)) ) :
//                ResponseEntity.ok( playerMapper.playerToPlayerSimpleDTO(playerService.findById(id)) ));*/
//        return ResponseEntity.ok(playerMapper.playerToPlayerAdminDTO( playerService.findById(player_id) ));
//    }
//
//    @Operation(summary = "Creates a new player.")
//    @ApiResponses( value = {
//            @ApiResponse( responseCode = "201", description = "Player created", content = { @Content }),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
//            @ApiResponse( responseCode = "404", description = "User, squad member or game not found", content = { @Content })
//    })
//    @PostMapping//POST: localhost:8080/api/players
//    public ResponseEntity createPlayer(@RequestBody PlayerPostDTO player) throws URISyntaxException {
//        playerService.add( playerMapper.playerPostDTOtoPlayer(player) ) ; //adds a new player
//        int player_id = playerMapper.playerPostDTOtoPlayer(player).getPlayer_id();
//        URI uri = new URI("api/players/" + player_id);  //making a new uri with the new players id
//        return ResponseEntity.created(uri).build();
//    }
//
//    @Operation(summary = "Updates the player with the given id.")
//    @ApiResponses( value = {
//            @ApiResponse( responseCode = "204", description = "Player updated", content = { @Content }),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
//            @ApiResponse( responseCode = "404", description = "Player not found", content = { @Content })
//    })
//    @PutMapping("{player_id}")//PUT: localhost:8080/api/players/id
//    public ResponseEntity updatePlayerById(@RequestBody PlayerPutDTO player, @PathVariable int player_id){
////        if (player_id != player.getPlayer_id())             //checking if the given id is not name as the given player id
////            return  ResponseEntity.badRequest().build();    //if ids are different returns bad request response
//        if ( isNull( playerService.findById(player_id)) )   //checking if the requested mission exists
//            return ResponseEntity.notFound().build();       //it is not exists so return notFound exception
//        playerService.update( playerMapper.playerPutDTOtoPlayer(player) ); //ids are same so call the update
//        return ResponseEntity.noContent().build();
//    }
//
//    @Operation(summary = "Deletes the player with the given id.")
//    @ApiResponses(value = {
//            @ApiResponse( responseCode =  "200", description = "Player deleted", content = { @Content}),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
//            @ApiResponse( responseCode = "404",description = "Player with supplied id, does not exist! ",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ProblemDetail.class)))})
//    @DeleteMapping("{player_id}")//DELETE: localhost:8080/api/players/id
//    public ResponseEntity deletePlayerById(@PathVariable int player_id){
//        playerService.deleteById(player_id);
//        return ResponseEntity.ok("Player deleted successfully!");
//    }
}
