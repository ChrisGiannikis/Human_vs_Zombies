package com.example.human_vs_zombies.exceptions;

public class MissionNotFoundException extends EntityNotFoundException{
    public MissionNotFoundException(int id) { super("Player does not exist with ID: " + id); }
}
