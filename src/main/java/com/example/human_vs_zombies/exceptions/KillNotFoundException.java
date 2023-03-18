package com.example.human_vs_zombies.exceptions;

public class KillNotFoundException extends EntityNotFoundException{

    public KillNotFoundException(int id){ super("Kill does not exist with ID: " + id);}
}
