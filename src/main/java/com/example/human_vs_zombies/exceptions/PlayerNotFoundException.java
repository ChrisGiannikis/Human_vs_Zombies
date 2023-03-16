package com.example.human_vs_zombies.exceptions;

public class PlayerNotFoundException extends EntityNotFoundException{
    public PlayerNotFoundException(int id){ super("Player does not exist with ID: " + id);}
}
