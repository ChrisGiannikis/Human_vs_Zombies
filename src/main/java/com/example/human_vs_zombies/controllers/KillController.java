package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.kill.KillDTO;
import com.example.human_vs_zombies.dto.kill.KillPostDTO;
import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.mappers.KillMapper;
import com.example.human_vs_zombies.services.game.GameService;
import com.example.human_vs_zombies.services.kill.KillService;
import com.example.human_vs_zombies.services.player.PlayerService;
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

    @GetMapping("/kill")
    public ResponseEntity findAll(){
        return ResponseEntity.ok(killMapper.killsToKillsDTO(killService.findAll()));
    }

    //get kill by id
    @GetMapping("/kill/{kill_id}")
    public ResponseEntity findById(@PathVariable("kill_id") int id){
        return ResponseEntity.ok(killMapper.killToKillDTO(killService.findById(id)));
    }

    // create a kill

    @PostMapping("/kill")
    public ResponseEntity createKill(@RequestBody KillPostDTO kill){

        return ResponseEntity.ok(killService.add(killMapper.KillPostDTOToKill(kill)));

    }

    //update a kill
    @PutMapping("/kill/{kill_id}")
    public ResponseEntity updateKillById(@RequestBody KillDTO kill, @PathVariable("kill_id") int id){
        return ResponseEntity.ok((killService.update(killMapper.killDTOToKill(kill))));

    }

    @DeleteMapping("/kill/{kill_id}")
    public ResponseEntity deleteKillById(@PathVariable("kill_id") int id){
        if(isNull(killService.findById(id)))
            return ResponseEntity.notFound().build();


        killService.deleteById(id);
        return ResponseEntity.ok("Kill deleted successfully!");
    }



}
