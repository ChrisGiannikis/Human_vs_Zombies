package com.example.human_vs_zombies.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SquadNotFoundException extends  RuntimeException{
    public SquadNotFoundException(int id){
        super("Squad does not exist with ID: " + id);
    }
}
