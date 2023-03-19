package com.example.human_vs_zombies.exceptions;

public class UserNotFoundException extends EntityNotFoundException{
    public UserNotFoundException(int id) { super("User does not exist with ID: " + id); }
}
