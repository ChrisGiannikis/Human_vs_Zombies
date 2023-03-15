package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.GameDTO;
import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.mappers.GameMapper;
import com.example.human_vs_zombies.services.game.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping(path = "api/v1/games")
public class GameController {

    private final GameService gameService;
    private final GameMapper gameMapper;

    public GameController(GameService gameService, GameMapper gameMapper){
        this.gameService = gameService;
        this.gameMapper = gameMapper;
    }

    @GetMapping//GET: localhost:8080/api/v1/games
    public ResponseEntity<Collection<GameDTO>> getAll(){
        Collection<GameDTO> gameDTOS = gameMapper.gameToGameDto(gameService.findAll());
        return ResponseEntity.ok(gameDTOS);
    }

    @GetMapping("{id}")//GET: localhost:8080/api/v1/games/id
    public ResponseEntity<GameDTO> getById(@PathVariable int id){
        GameDTO gameDTO = gameMapper.gameToGameDto(gameService.findById(id));
        return ResponseEntity.ok(gameDTO);
    }

    @PostMapping//POST: localhost:8080/api/v1/games
    public ResponseEntity<GameDTO> add(@RequestBody GameDTO gameDTO){
        gameService.add(gameMapper.gameDtoToGame(gameDTO));
        URI location = URI.create("games/" + gameDTO.getGame_id());
        return ResponseEntity.created(location).build();
    }

    @PutMapping({"id"})//PUT: localhost:8080/api/v1/games/id
    public ResponseEntity<GameDTO> update(@RequestBody GameDTO gameDTO, @PathVariable int id){
        if(id!=gameDTO.getGame_id()){
            return ResponseEntity.badRequest().build();
        }

        gameService.update(gameMapper.gameDtoToGame(gameDTO));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping({"id"})//DELETE: localhost:8080/api/v1/games/id
    public ResponseEntity<Game> delete(@PathVariable int id){
        //ISN'T WORKING YET!! gameService.deleteById() is empty!!
        gameService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
