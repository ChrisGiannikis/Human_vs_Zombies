package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.SquadDTO;
import com.example.human_vs_zombies.mappers.SquadMapper;
import com.example.human_vs_zombies.services.squad.SquadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

import static java.util.Objects.isNull;

@RestController
@RequestMapping(path="api/v1/squads")
public class SquadController {

    private final SquadService squadService;
    private final SquadMapper squadMapper;

    public SquadController(SquadService squadService, SquadMapper squadMapper) {
        this.squadService = squadService;
        this.squadMapper = squadMapper;
    }

    @Operation(summary = "Get all squads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadDTO.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Did not find any squads",
                    content = @Content)
    })
    @GetMapping//GET: localhost:8080/api/v1/squads
    public ResponseEntity<Collection<SquadDTO>> getAll() {
        Collection<SquadDTO> squadDTOS = squadMapper.squadToSquadDto(squadService.findAll());
        if(squadDTOS.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(squadDTOS);
    }

    @Operation(summary = "Get a squad by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Squad does not exist with supplied ID",
                    content = @Content)

    })
    @GetMapping("{id}")//GET: localhost:8080/api/v1/squads/id
    public ResponseEntity<SquadDTO> getById(@PathVariable int id) {
        SquadDTO squadDTO = squadMapper.squadToSquadDto(squadService.findById(id));
        return ResponseEntity.ok(squadDTO);
    }

    @Operation(summary = "Add a squad")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Squad successfully added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "The given Game does not exists!",
                    content = @Content)
    })
    @PostMapping//POST: localhost:8080/api/v1/squads
    public ResponseEntity<SquadDTO> add(@RequestBody SquadDTO squadDTO) {
        if (isNull(squadDTO.getName()))
            return ResponseEntity.badRequest().build();
        squadService.add(squadMapper.squadDtoToSquad(squadDTO));
        URI location = URI.create("squads/" + squadDTO.getSquad_id());
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Updates a squad")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Squad successfully updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Squad not found with supplied ID",
                    content = @Content)
    })
    @PutMapping({"{id}"})//PUT: localhost:8080/api/v1/squads/id
    public ResponseEntity<SquadDTO> update(@RequestBody SquadDTO squadDTO, @PathVariable int id) {
        if (id != squadDTO.getSquad_id()) {
            return ResponseEntity.badRequest().build();
        }

        squadService.update(squadMapper.squadDtoToSquad(squadDTO));

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a squad by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Squad successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Squad not found with supplied ID",
                    content = @Content)
    })
    @DeleteMapping({"{id}"})//DELETE: localhost:8080/api/v1/squads/id
    public ResponseEntity<SquadDTO> delete(@PathVariable int id) {
        SquadDTO squadDTO = squadMapper.squadToSquadDto(squadService.findById(id));
        squadService.deleteById(squadDTO.getSquad_id());
        return ResponseEntity.noContent().build();
    }
}
