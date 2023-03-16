package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.SquadDTO;
import com.example.human_vs_zombies.mappers.SquadMapper;
import com.example.human_vs_zombies.services.squad.SquadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping(path="api/v1/squads")
public class SquadController {

    private final SquadService squadService;
    private final SquadMapper squadMapper;

    public SquadController(SquadService squadService, SquadMapper squadMapper) {
        this.squadService = squadService;
        this.squadMapper = squadMapper;
    }

    @GetMapping//GET: localhost:8080/api/v1/squads
    public ResponseEntity<Collection<SquadDTO>> getAll() {
        Collection<SquadDTO> squadDTOS = squadMapper.squadToSquadDto(squadService.findAll());
        return ResponseEntity.ok(squadDTOS);
    }

    @GetMapping("{id}")//GET: localhost:8080/api/v1/squads/id
    public ResponseEntity<SquadDTO> getById(@PathVariable int id) {
        SquadDTO squadDTO = squadMapper.squadToSquadDto(squadService.findById(id));
        return ResponseEntity.ok(squadDTO);
    }

    @PostMapping//POST: localhost:8080/api/v1/squads
    public ResponseEntity<SquadDTO> add(@RequestBody SquadDTO squadDTO) {
        squadService.add(squadMapper.squadDtoToSquad(squadDTO));
        URI location = URI.create("squads/" + squadDTO.getSquad_id());
        return ResponseEntity.created(location).build();
    }

    @PutMapping({"id"})//PUT: localhost:8080/api/v1/squads/id
    public ResponseEntity<SquadDTO> update(@RequestBody SquadDTO squadDTO, @PathVariable int id) {
        if (id != squadDTO.getSquad_id()) {
            return ResponseEntity.badRequest().build();
        }

        squadService.update(squadMapper.squadDtoToSquad(squadDTO));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping({"id"})//DELETE: localhost:8080/api/v1/games/id
    public ResponseEntity<SquadDTO> delete(@PathVariable int id) {
        //ISN'T WORKING YET!! squadService.deleteById() is empty!!
        squadService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
