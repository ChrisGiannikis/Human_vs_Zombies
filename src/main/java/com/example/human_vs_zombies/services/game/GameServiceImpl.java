package com.example.human_vs_zombies.services.game;

import com.example.human_vs_zombies.entities.Game;
import com.example.human_vs_zombies.entities.Kill;
import com.example.human_vs_zombies.entities.Player;
import com.example.human_vs_zombies.exceptions.GameNotFoundException;
import com.example.human_vs_zombies.exceptions.KillNotFoundException;
import com.example.human_vs_zombies.exceptions.PlayerNotFoundException;
import com.example.human_vs_zombies.repositories.GameRepository;
import com.example.human_vs_zombies.repositories.KillRepository;
import com.example.human_vs_zombies.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static java.util.Objects.isNull;

@Service
public class GameServiceImpl implements GameService{

    private final GameRepository gameRepository;
    private final KillRepository killRepository;

    private final PlayerRepository playerRepository;

    public GameServiceImpl(GameRepository gameRepository, KillRepository killRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.killRepository = killRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public Game findById(Integer id) {
        return gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
    }

    @Override
    public Collection<Game> findAll() {
        return gameRepository.findAll();
    }
    @Override
    public Game add(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public Game update(Game updatedGame) {
        Game game = this.findById(updatedGame.getGame_id());
        updatedGame.setMissions(game.getMissions());
        updatedGame.setPlayers(game.getPlayers());
        updatedGame.setSquads(game.getSquads());
        return gameRepository.save(updatedGame);
    }

    @Override
    public void deleteById(Integer id) {
       gameRepository.deleteById(id);
    }

    @Override
    public Player findPlayerById(int game_id, int player_id) {
        Game game = gameRepository.findById(game_id).orElseThrow(() -> new GameNotFoundException(game_id));
        Player player = playerRepository.findById(player_id).orElseThrow(() -> new PlayerNotFoundException(player_id));
        Collection<Player> players = game.getPlayers();

        if(players.contains(player)){
            return player;
        }

        return null;
    }

    @Override
    public void addPlayer(int game_id, Player player) {
        Game game = gameRepository.findById(game_id).orElseThrow(() -> new GameNotFoundException(game_id));
        Set<Player> players = game.getPlayers();
        players.add(player);
        game.setPlayers(players);

        playerRepository.save(player);
    }

    @Override
    public void updatePlayer(int gameId, int playerId, Player updatedPlayer) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        updatedPlayer.setPlayer_id(player.getPlayer_id());
        updatedPlayer.setGame(player.getGame());
        updatedPlayer.setChat(player.getChat());
        updatedPlayer.setSquadMember(player.getSquadMember());
        updatedPlayer.setDeath(player.getDeath());
        updatedPlayer.setKills(player.getKills());
        updatedPlayer.setUser(player.getUser());
        Set<Player> players = game.getPlayers();
        players.remove(player);
        players.add(updatedPlayer);
        game.setPlayers(players);
        gameRepository.save(game);

        playerRepository.save(updatedPlayer);
    }

    @Override
    public Collection<Kill> findAllKills(int gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        Collection<Kill> kills = new java.util.HashSet<>(Collections.emptySet());
        Set<Player> players = game.getPlayers();
        for (Player p: players){
            if(!isNull(p.getDeath()) && p.getGame().getGame_id()==gameId){
                kills.add(p.getDeath());
            }
        }

        return kills;
    }

    @Override
    public Kill findKillById(int game_id, int kill_id) {
        Game game = gameRepository.findById(game_id).orElseThrow(() -> new GameNotFoundException(game_id));
        Kill kill = killRepository.findById(kill_id).orElseThrow(() -> new KillNotFoundException(kill_id));
        Set<Player> players = game.getPlayers();
        for (Player p: players){
            if((!isNull(p.getDeath())) && p.getDeath().getKill_id() == kill_id){
                return kill;
            }
        }
        return null;
    }

    @Override
    public void deleteKillById(int killId) {
        if(killRepository.existsById(killId)){
            killRepository.deleteById(killId);
        } else{
            throw new KillNotFoundException(killId);
        }
    }

}
