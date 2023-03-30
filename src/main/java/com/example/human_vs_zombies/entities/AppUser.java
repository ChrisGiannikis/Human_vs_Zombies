package com.example.human_vs_zombies.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
public class AppUser {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String user_id;

    @Column(length = 40, nullable = false)
    private String first_name;

    @Column(length = 40)
    private String last_name;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Player player;

}
