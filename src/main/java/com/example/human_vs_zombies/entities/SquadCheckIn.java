package com.example.human_vs_zombies.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class SquadCheckIn {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int squad_checkin_id;

    @Column
    private ZonedDateTime start_time;

    @Column
    private ZonedDateTime end_time;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    @ManyToOne
    @JoinColumn(name = "squad_member_id")
    private SquadMember squadMember;
}
