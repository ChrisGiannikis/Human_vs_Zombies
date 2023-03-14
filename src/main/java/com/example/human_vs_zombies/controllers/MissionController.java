package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.entities.Mission;
import com.example.human_vs_zombies.mappers.MissionMapper;
import com.example.human_vs_zombies.services.mission.MissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/missions")
public class MissionController {

    private final MissionService missionService;
    private final MissionMapper missionMapper;

    public MissionController(MissionService missionService, MissionMapper missionMapper) {
        this.missionService = missionService;
        this.missionMapper = missionMapper;
    }

    @GetMapping
    public ResponseEntity findAll(){
        //check for missions that belongs to the faction of the requested user
        return ResponseEntity.ok( missionMapper.missonToMissionDTO( missionService.findAll() ) );
    }

    @GetMapping("{mission_id}")
    public ResponseEntity findById(@PathVariable int mission_id){
        //check for faction, if a player request a mission of another faction return 403 Forbidden
        return ResponseEntity.ok( missionMapper.missonToMissionDTO( missionService.findById(mission_id) ) );
    }

    @PostMapping
    public ResponseEntity createMission(@RequestBody Mission mission) throws URISyntaxException {
        //Admin only
        missionService.add(mission); //adds the given new mission
        URI uri = new URI("api/missions" + mission.getMission_id()); //creating a new uri for the new mission
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("{mission_id}")
    public ResponseEntity updateMission(@RequestBody Mission mission, @PathVariable int mission_id){
        //Admin only
        if(mission_id != mission.getMission_id()) //if the given id is not name as the given mission id
            return ResponseEntity.badRequest().build(); // they are different and returns bad request response
        missionMapper.missonToMissionDTO(  missionService.update(mission) ); // ids are same so call the update
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{mission_id}")
    public ResponseEntity deleteMission(@PathVariable int mission_id){
        //Admin only
        missionService.deleteById(mission_id);
        return ResponseEntity.ok("Mission deleted successfully!");
    }
}
