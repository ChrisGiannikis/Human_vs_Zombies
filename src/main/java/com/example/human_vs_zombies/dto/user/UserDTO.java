package com.example.human_vs_zombies.dto.user;

import com.example.human_vs_zombies.entities.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private int user_id;
    private  String keycloak_id;
    private String first_name;
    private String last_name;
    private int player;
}
