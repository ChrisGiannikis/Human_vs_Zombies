package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.squadMember.SquadMemberDTO;
import com.example.human_vs_zombies.dto.squadMember.SquadMemberPostDTO;
import com.example.human_vs_zombies.dto.squadMember.SquadMemberPutDTO;
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
                            schema = @Schema(implementation = SquadMemberDTO.class)) }),
            @ApiResponse(responseCode = "404",
                    description = "Did not find any squad members",
                    content = @Content)
    })
    @GetMapping//GET: localhost:8080/api/v1/squadmembers
    public ResponseEntity<Collection<SquadMemberDTO>> getAll(){
        Collection<SquadMemberDTO> squadMemberDTOS = squadMemberMapper.squadToSquadDto(squadMemberService.findAll());
        if (squadMemberDTOS.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(squadMemberDTOS);
    }

    @Operation(summary = "Get a squad member by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadMemberDTO.class))}),
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
            @ApiResponse(responseCode = "404",
                    description = "The given Squad or Player does not exists!",
                    content = @Content)
    })
    @PostMapping//POST: localhost:8080/api/v1/squadmembers
    public ResponseEntity<SquadMemberDTO> add(@RequestBody SquadMemberPostDTO squadMemberPostDTO){
        squadMemberService.add(squadMemberMapper.squadMemberPostDtoToSquadMember(squadMemberPostDTO));
        int squadMember_id = squadMemberMapper.squadMemberPostDtoToSquadMember(squadMemberPostDTO).getSquad_member_id();
        URI location = URI.create("squadmembers/" + squadMember_id);
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
    @PutMapping({"{id}"})//PUT: localhost:8080/api/v1/squadmembers/id
    public ResponseEntity<SquadMemberDTO> update(@RequestBody SquadMemberPutDTO squadMemberPutDTO, @PathVariable int id){
        if(id != squadMemberPutDTO.getSquad_member_id()){
            return ResponseEntity.badRequest().build();
        }

        squadMemberService.update(squadMemberMapper.squadMemberPutDtoToSquadMember(squadMemberPutDTO));

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
    @DeleteMapping({"{id}"})//DELETE: localhost:8080/api/v1/squadmembers/id
    public ResponseEntity<SquadMemberDTO> delete(@PathVariable int id){
        SquadMemberDTO squadMemberDTO = squadMemberMapper.squadMemberToSquadMemberDto(squadMemberService.findById(id));
        squadMemberService.deleteById(squadMemberDTO.getSquad_member_id());
        return ResponseEntity.noContent().build();
    }
}
