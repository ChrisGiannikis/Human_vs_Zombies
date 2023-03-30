package com.example.human_vs_zombies.services.user;

import com.example.human_vs_zombies.entities.AppUser;
import com.example.human_vs_zombies.exceptions.UserNotFoundException;
import com.example.human_vs_zombies.repositories.UserRepository;
import com.example.human_vs_zombies.services.player.PlayerService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PlayerService playerService;

    public UserServiceImpl(UserRepository userRepository, PlayerService playerService) {
        this.userRepository = userRepository;
        this.playerService = playerService;
    }

    @Override
    public AppUser findById(Integer id) { return null;  /*userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));*/ }

    @Override
    public AppUser findByIdStr(String id) { return userRepository.findByIdStr(id); }


    @Override
    public AppUser findByIdStr(String id) { return userRepository.findByIdStr(id); }


    @Override
    public Collection<AppUser> findAll() { return userRepository.findAll(); }

    @Override
    public AppUser add(AppUser entity) { return userRepository.save(entity); }

    @Override
    public AppUser update(AppUser entity) { return userRepository.save(entity); }

    @Override
    public void deleteById(Integer id) {
//        //check if user exists
//        this.findById(id);
//        //delete the user
//        userRepository.deleteById(id);
    }
}
