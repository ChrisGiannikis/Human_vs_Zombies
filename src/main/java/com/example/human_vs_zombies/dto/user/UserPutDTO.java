package com.example.human_vs_zombies.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPutDTO {
    private int user_id;
    private String first_name;
    private String last_name;
    private boolean is_administrator;
}
