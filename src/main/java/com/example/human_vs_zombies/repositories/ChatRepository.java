package com.example.human_vs_zombies.repositories;

import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.enums.ChatScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("select c from Chat c join Squad s on c.squad.squad_id = s.squad_id join Game g on s.game.game_id = g.game_id where g.game_id = ?1 and s.squad_id = ?2 and c.chatScope like ?3")
    Collection<Chat> findAllSquadChatByGameId(int gameId, int squadId, ChatScope scope);

    @Query("select c from Chat c join Player p on c.player.player_id = p.player_id join Game g on p.game.game_id = g.game_id where g.game_id = ?1 and c.chatScope like ?2 or (c.chatScope = ?3 and p.human = ?4)")
    Collection<Chat> findAllNonSquadChatByGameId(int gameId, ChatScope global, ChatScope faction, boolean isHuman);

    @Query("select c from Chat c join Player p on c.player.player_id = p.player_id join Game g on p.game.game_id = g.game_id where g.game_id = ?1 and c.chatScope not like ?2")
    Collection<Chat> findAllNonSquadChatByGameIdAdmin(int gameId, ChatScope squad);
}
