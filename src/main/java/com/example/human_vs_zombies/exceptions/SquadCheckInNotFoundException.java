package com.example.human_vs_zombies.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SquadCheckInNotFoundException extends EntityNotFoundException{
    public SquadCheckInNotFoundException(int id) { super( "Squad check-in does not exist with ID: " + id);}
}
