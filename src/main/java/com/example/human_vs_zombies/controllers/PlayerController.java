package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.mappers.PlayerMapper;
import com.example.human_vs_zombies.services.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/players")
public class PlayerController {

    private final PlayerService playerService;
//    private final PlayerMapper playerMapper;

    public PlayerController(PlayerService playerService) {   //, PlayerMapper playerMapper) {
        this.playerService = playerService;
        //this.playerMapper = playerMapper;
    }

    @GetMapping
    public ResponseEntity findAll(Boolean is_administrator){
        /*//if request has been from admin use the playerToPlayerAdminDTO else use the playerToPlayerSimpleDTO
        return (ResponseEntity) (is_administrator ?
                ResponseEntity.ok( playerMapper.playerToPlayerAdminDTO(playerService.findAll()) ) :
                ResponseEntity.ok( playerMapper.playerToPlayerSimpleDTO(playerService.findAll()) ));*/
        return ResponseEntity.ok( playerService.findAll() );
    }

    @GetMapping("{id}")
    public ResponseEntity findById(@PathVariable int id, Boolean is_administrator){
       /* //if request has been from admin use the playerToPlayerAdminDTO else use the playerToPlayerSimpleDTO
        return (ResponseEntity) (is_administrator ?
                ResponseEntity.ok( playerMapper.playerToPlayerAdminDTO( playerService.findById(id)) ) :
                ResponseEntity.ok( playerMapper.playerToPlayerSimpleDTO(playerService.findById(id)) ));*/
        return ResponseEntity.ok(playerService.findById(id));
    }

    @PostMapping
    public ResponseEntity addPlayer(@RequestBody Player player) throws URISyntaxException {
        playerService.add(player); //adds a new player
        URI uri = new URI("api/players/" + player.getPlayer_id());  //making a new uri with the new players id
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("{id}")
    public ResponseEntity updatePlayerById(@RequestBody Player player, @PathVariable int id){
        if (id != player.getPlayer_id())  //checking if the given id is same with the given id
            return  ResponseEntity.badRequest().build();  //if ids are different returns bad request response
        playerService.updatePlayerById(player,id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletePlayerById(@PathVariable int id){
        playerService.deleteById(id);
        return ResponseEntity.ok("Player deleted successfully!");
    }
}
