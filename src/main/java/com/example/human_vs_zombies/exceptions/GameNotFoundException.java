package com.example.human_vs_zombies.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class GameNotFoundException  extends RuntimeException{
    public GameNotFoundException(int id){
        super("Game does not exist with ID: " + id);
    }
}
