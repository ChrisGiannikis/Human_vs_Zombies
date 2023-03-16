package com.example.human_vs_zombies.services.user;

import com.example.human_vs_zombies.entities.AppUser;
import com.example.human_vs_zombies.exceptions.UserNotFoundException;
import com.example.human_vs_zombies.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUser findById(Integer id) { return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)); }

    @Override
    public Collection<AppUser> findAll() { return userRepository.findAll(); }

    @Override
    public AppUser add(AppUser entity) { return userRepository.save(entity); }

    @Override
    public AppUser update(AppUser entity) { return userRepository.save(entity); }

    @Override
    public void deleteById(Integer id) {
        //check for foreign keys
        userRepository.deleteById(id);
    }
}
