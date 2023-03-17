package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.SquadDTO;
import com.example.human_vs_zombies.dto.SquadMemberDTO;
import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.mappers.SquadMemberMapper;
import com.example.human_vs_zombies.services.squadMember.SquadMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all squad members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Did not find any squad members",
                    content = @Content)
    })
    @GetMapping//GET: localhost:8080/api/v1/squadmembers
    public ResponseEntity<Collection<SquadMemberDTO>> getAll(){
        Collection<SquadMemberDTO> squadMemberDTOS = squadMemberMapper.squadToSquadDto(squadMemberService.findAll());
        return ResponseEntity.ok(squadMemberDTOS);
    }

    @Operation(summary = "Get a squad member by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Squad member does not exist with supplied ID",
                    content = @Content)

    })
    @GetMapping("{id}")//GET: localhost:8080/api/v1/squadmembers/id
    public ResponseEntity<SquadMemberDTO> getById(@PathVariable int id){
        SquadMemberDTO squadMemberDTO = squadMemberMapper.squadMemberToSquadMemberDto(squadMemberService.findById(id));
        return ResponseEntity.ok(squadMemberDTO);
    }

    @Operation(summary = "Add a squad member")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Squad member successfully added",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
    })
    @PostMapping//POST: localhost:8080/api/v1/squadmembers
    public ResponseEntity<SquadMemberDTO> add(@RequestBody SquadMemberDTO squadMemberDTO){
        squadMemberService.add(squadMemberMapper.squadMemberDtoToSquad(squadMemberDTO));
        URI location = URI.create("squadmembers/" + squadMemberDTO.getSquad_member_id());
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Updates a squad member")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Squad member successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Squad member not found with supplied ID",
                    content = @Content)
    })
    @PutMapping({"id"})//PUT: localhost:8080/api/v1/squadmembers/id
    public ResponseEntity<SquadMemberDTO> update(@RequestBody SquadMemberDTO squadMemberDTO, @PathVariable int id){
        if(id!=squadMemberDTO.getSquad_member_id()){
            return ResponseEntity.badRequest().build();
        }

        squadMemberService.update(squadMemberMapper.squadMemberDtoToSquad(squadMemberDTO));

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a squad member by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "Squad member successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Malformed request",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Squad member not found with supplied ID",
                    content = @Content)
    })
    @DeleteMapping({"id"})//DELETE: localhost:8080/api/v1/squadmembers/id
    public ResponseEntity<SquadDTO> delete(@PathVariable int id){
        //ISN'T WORKING YET!! squadMemberService.deleteById() is empty!!
        squadMemberService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
