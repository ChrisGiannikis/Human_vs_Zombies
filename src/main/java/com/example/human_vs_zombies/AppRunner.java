package com.example.human_vs_zombies;

import com.example.human_vs_zombies.services.game.GameService;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    private final GameService gameService;

    public AppRunner(GameService gameService){
        this.gameService = gameService;
    }
    @Override
    @Transactional
    public  void run(ApplicationArguments args) throws  Exception{
        System.out.println(gameService.findAll());
    }
}
