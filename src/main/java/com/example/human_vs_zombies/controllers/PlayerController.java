package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.mappers.PlayerMapper;
import com.example.human_vs_zombies.services.player.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class PlayerController {
    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    public PlayerController(PlayerService playerService, PlayerMapper playerMapper) {
        this.playerService = playerService;
        this.playerMapper = playerMapper;
    }

    @GetMapping
    public ResponseEntity findAll(Boolean is_administrator){
        //if request has been from admin use the playerToPlayerAdminDTO else use the playerToPlayerSimpleDTO
        return (ResponseEntity) (is_administrator ?
                ResponseEntity.ok( playerMapper.playerToPlayerAdminDTO(playerService.findAll()) ) :
                ResponseEntity.ok( playerMapper.playerToPlayerSimpleDTO(playerService.findAll()) ));
    }

    @GetMapping("{id}")
    public ResponseEntity findById(@PathVariable int id, Boolean is_administrator){
        //if request has been from admin use the playerToPlayerAdminDTO else use the playerToPlayerSimpleDTO
        return (ResponseEntity) (is_administrator ?
                ResponseEntity.ok( playerMapper.playerToPlayerAdminDTO( playerService.findById(id)) ) :
                ResponseEntity.ok( playerMapper.playerToPlayerSimpleDTO(playerService.findById(id)) ));
    }
}
