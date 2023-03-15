package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.player.PlayerPostDTO;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.mappers.PlayerMapper;
import com.example.human_vs_zombies.services.player.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/players")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    public PlayerController(PlayerService playerService, PlayerMapper playerMapper) {
        this.playerService = playerService;
        this.playerMapper = playerMapper;
    }

    @GetMapping
    public ResponseEntity findAll(Boolean is_administrator){
        /*//if request has been from admin use the playerToPlayerAdminDTO else use the playerToPlayerSimpleDTO
        return (ResponseEntity) (is_administrator ?
                ResponseEntity.ok( playerMapper.playerToPlayerAdminDTO(playerService.findAll()) ) :
                ResponseEntity.ok( playerMapper.playerToPlayerSimpleDTO(playerService.findAll()) ));*/
        return ResponseEntity.ok( playerMapper.playerToPlayerAdminDTO(playerService.findAll()) );
    }

    @GetMapping("{player_id}")
    public ResponseEntity findById(@PathVariable int player_id, Boolean is_administrator){
       /* //if request has been from admin use the playerToPlayerAdminDTO else use the playerToPlayerSimpleDTO
        return (ResponseEntity) (is_administrator ?
                ResponseEntity.ok( playerMapper.playerToPlayerAdminDTO( playerService.findById(id)) ) :
                ResponseEntity.ok( playerMapper.playerToPlayerSimpleDTO(playerService.findById(id)) ));*/
        return ResponseEntity.ok(playerMapper.playerToPlayerAdminDTO( playerService.findById(player_id)));
    }

    @PostMapping
    public ResponseEntity createPlayer(@RequestBody PlayerPostDTO player) throws URISyntaxException {
        playerService.add( playerMapper.playerPostDTOtoPlayer(player) ) ; //adds a new player
        URI uri = new URI("api/players/" + player.getPlayer_id());  //making a new uri with the new players id
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("{player_id}")
    public ResponseEntity updatePlayerById(@RequestBody PlayerPostDTO player, @PathVariable int player_id){
        if (player_id != player.getPlayer_id())  //checking if the given id is not name as the given player id
            return  ResponseEntity.badRequest().build();  //if ids are different returns bad request response
        playerService.update( playerMapper.playerPostDTOtoPlayer(player) ); //ids are same so call the update
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{player_id}")
    public ResponseEntity deletePlayerById(@PathVariable int player_id){
        playerService.deleteById(player_id);
        return ResponseEntity.ok("Player deleted successfully!");
    }
}
