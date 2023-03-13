package com.example.human_vs_zombies.services.kill;

import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.services.CrudService;
import org.springframework.stereotype.Service;

@Service
public interface KillService extends CrudService<Kill,Integer> {
    public Kill updateKillById(Kill kill,int id);
}
