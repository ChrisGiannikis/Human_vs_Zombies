package com.example.human_vs_zombies.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column(length = 40, nullable = false)
    private String first_name;

    @Column(length = 40)
    private String last_name;

    @Column
    private boolean is_administrator;

    @OneToOne(mappedBy = "user")
    private Player player;

}
