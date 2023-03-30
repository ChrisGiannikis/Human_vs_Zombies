package com.example.human_vs_zombies.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SquadMemberNotFoundException extends RuntimeException{
    public SquadMemberNotFoundException(int id){
        super("Squad member does not exist with id: " + id);
    }
}
