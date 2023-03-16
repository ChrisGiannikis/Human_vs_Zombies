package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.SquadDTO;
import com.example.human_vs_zombies.dto.SquadMemberDTO;
import com.example.human_vs_zombies.mappers.SquadMemberMapper;
import com.example.human_vs_zombies.services.squadMember.SquadMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping(path="api/v1/squadmembers")
public class SquadMemberController {

    private final SquadMemberService squadMemberService;
    private final SquadMemberMapper squadMemberMapper;

    public SquadMemberController(SquadMemberService squadMemberService, SquadMemberMapper squadMemberMapper) {
        this.squadMemberService = squadMemberService;
        this.squadMemberMapper = squadMemberMapper;
    }

    @GetMapping//GET: localhost:8080/api/v1/squadmembers
    public ResponseEntity<Collection<SquadMemberDTO>> getAll(){
        Collection<SquadMemberDTO> squadMemberDTOS = squadMemberMapper.squadToSquadDto(squadMemberService.findAll());
        return ResponseEntity.ok(squadMemberDTOS);
    }

    @GetMapping("{id}")//GET: localhost:8080/api/v1/squadmembers/id
    public ResponseEntity<SquadMemberDTO> getById(@PathVariable int id){
        SquadMemberDTO squadMemberDTO = squadMemberMapper.squadMemberToSquadMemberDto(squadMemberService.findById(id));
        return ResponseEntity.ok(squadMemberDTO);
    }

    @PostMapping//POST: localhost:8080/api/v1/squadmembers
    public ResponseEntity<SquadMemberDTO> add(@RequestBody SquadMemberDTO squadMemberDTO){
        squadMemberService.add(squadMemberMapper.squadMemberDtoToSquad(squadMemberDTO));
        URI location = URI.create("squadmembers/" + squadMemberDTO.getSquad_member_id());
        return ResponseEntity.created(location).build();
    }

    @PutMapping({"id"})//PUT: localhost:8080/api/v1/squadmembers/id
    public ResponseEntity<SquadMemberDTO> update(@RequestBody SquadMemberDTO squadMemberDTO, @PathVariable int id){
        if(id!=squadMemberDTO.getSquad_member_id()){
            return ResponseEntity.badRequest().build();
        }

        squadMemberService.update(squadMemberMapper.squadMemberDtoToSquad(squadMemberDTO));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping({"id"})//DELETE: localhost:8080/api/v1/games/id
    public ResponseEntity<SquadDTO> delete(@PathVariable int id){
        //ISN'T WORKING YET!! squadMemberService.deleteById() is empty!!
        squadMemberService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
