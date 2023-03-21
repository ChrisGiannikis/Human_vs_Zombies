package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.squadMember.SquadMemberDTO;
import com.example.human_vs_zombies.dto.squadMember.SquadMemberPostDTO;
import com.example.human_vs_zombies.dto.squadMember.SquadMemberPutDTO;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.entities.Squad;
import com.example.human_vs_zombies.entities.SquadMember;
import com.example.human_vs_zombies.services.player.PlayerService;
import com.example.human_vs_zombies.services.squad.SquadService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class SquadMemberMapper {

    @Autowired
    protected SquadService squadService;

    @Autowired
    protected PlayerService playerService;

    @Mapping(target = "squad", source = "squad.squad_id")
    @Mapping(target = "player", source = "player.player_id")
    public abstract SquadMemberDTO squadMemberToSquadMemberDto(SquadMember squadMember);

    @Mapping(target = "squad", source = "squad", qualifiedByName = "squadIdToSquad")
    @Mapping(target = "player", source = "player", qualifiedByName = "playerIdToPlayer")
    @Mapping(target = "squadCheckIns", ignore = true)
    public abstract SquadMember squadMemberDtoToSquad(SquadMemberDTO squadMemberDTO);

    @Mapping(target = "squad", source = "squad", qualifiedByName = "squadIdToSquad")
    @Mapping(target = "player", source = "player", qualifiedByName = "playerIdToPlayer")
    public abstract SquadMember squadMemberPostDtoToSquadMember(SquadMemberPostDTO squadMemberPostDTO);

    @Mapping(target = "squad", source = "squad", qualifiedByName = "squadIdToSquad")
    @Mapping(target = "player", source = "player", qualifiedByName = "playerIdToPlayer")
    public abstract SquadMember squadMemberPutDtoToSquadMember(SquadMemberPutDTO squadMemberPutDTO);

    @Named("squadIdToSquad")
    Squad mapIdToSquad(int id){return squadService.findById(id);}

    @Named("playerIdToPlayer")
    Player mapIdToplayer(int id){return playerService.findById(id);}

    public abstract Collection<SquadMemberDTO> squadToSquadDto(Collection<SquadMember> squadMembers);
}
