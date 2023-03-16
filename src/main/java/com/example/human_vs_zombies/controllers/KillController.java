package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.services.kill.KillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class KillController {
    @Autowired
    private  KillService killService;

    public KillController(KillService killService) {
        this.killService = killService;
    }
    //get all kills
    @GetMapping("/kill")
    public ResponseEntity findAll(){
        return ResponseEntity.ok(killService.findAll());
    }

    //get kill by id
    @GetMapping("/kill/{kill_id}")
    public ResponseEntity findById(@PathVariable("kill_id") int id){
        return ResponseEntity.ok(killService.findById(id));
    }

    // create a kill

    @PostMapping("/kill")
    public ResponseEntity createKill(@RequestBody Kill kill){

        return ResponseEntity.ok(killService.add(kill));

    }

    //update a kill
    @PutMapping("/kill/{kill_id}")
    public ResponseEntity updateKillById(@RequestBody Kill kill,@PathVariable("kill_id") int id){
        return ResponseEntity.ok(killService.updateKillById(kill,id));

    }

    @DeleteMapping("/kill/{kill_id}")
    public ResponseEntity deleteKillById(@PathVariable("kill_id") int id){

        killService.deleteById(id);
        return ResponseEntity.ok("Kill deleted successfully!");
    }



}
