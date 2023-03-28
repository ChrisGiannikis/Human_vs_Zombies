package com.example.human_vs_zombies.mappers;

import com.example.human_vs_zombies.dto.squadCheckIn.SquadCheckInDTO;
import com.example.human_vs_zombies.dto.squadCheckIn.SquadCheckInPostDTO;
import com.example.human_vs_zombies.entities.SquadCheckIn;
import com.example.human_vs_zombies.services.squadMember.SquadMemberService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class SquadCheckInMapper {
    @Autowired
    protected SquadMemberService squadMemberService;

    @Mapping(target = "squadMember", source = "squadMember.squad_member_id")
    public abstract SquadCheckInDTO squadCheckInToSquadCheckInDTO(SquadCheckIn squadCheckIn);
    public abstract Collection<SquadCheckInDTO> squadCheckInToSquadCheckInDTO(Collection<SquadCheckIn> squadCheckIn);

    @Mapping(target = "squad_checkin_id", ignore = true)
    @Mapping(target = "lat", ignore = true)
    @Mapping(target = "lng", ignore = true)
    @Mapping(target = "squadMember", ignore = true)
    public abstract SquadCheckIn squadCheckInPostDTOToSquadCheckIn(SquadCheckInPostDTO squadCheckInPostDTO);

}
