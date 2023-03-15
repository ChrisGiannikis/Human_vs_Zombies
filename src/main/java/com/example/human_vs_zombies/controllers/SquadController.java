package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.entities.SquadCheckIn;
import com.example.human_vs_zombies.entities.SquadMember;
import com.example.human_vs_zombies.services.squadCheckIn.SquadCheckInSevice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/squad")
public class SquadController {
    private final SquadCheckInSevice squadCheckInSevice;

    public SquadController(SquadCheckInSevice squadCheckInSevice) {
        this.squadCheckInSevice = squadCheckInSevice;
    }

    @GetMapping("{squad_id}/check-in")
    public ResponseEntity getCheckIns(){
        //(Boolean is_administrator, SquadMember member, Player player)
        //Get a list of squad check-in markers.
        //Only administrators and members of a squad who are still in the appropriate faction may see squad check-ins
        return ResponseEntity.ok( squadCheckInSevice.findAll());
    }

    @PostMapping("{squad_id}/check-in")
    public ResponseEntity createCheckIn(@RequestBody SquadCheckIn squadCheckIn) throws URISyntaxException {
        //(SquadMember member, Player player)
        // creates a new checkin
        //Only members of a squad who are still in the appropriate faction may check-in with their squad
        squadCheckInSevice.add(squadCheckIn);
        URI uri = new URI("api/squad/" + "{squad_id}/check-in" + squadCheckIn.getSquad_checkin_id());  //making a new uri with the new players id
        return ResponseEntity.created(uri).build();
    }
}
