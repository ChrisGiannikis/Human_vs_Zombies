package com.example.human_vs_zombies.services.kill;

import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.services.CrudService;

<<<<<<< HEAD
import java.util.Collection;

@Service
=======
>>>>>>> f243b00c0efc9f51f6e419fde69d0c22dd586d8f
public interface KillService extends CrudService<Kill,Integer> {
    public Kill updateKillById(Kill kill,int id);
    public Collection<Kill> findKillsByGameId(Integer gameId);
}
