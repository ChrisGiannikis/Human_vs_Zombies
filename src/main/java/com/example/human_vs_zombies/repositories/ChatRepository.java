package com.example.human_vs_zombies.repositories;

import com.example.human_vs_zombies.entities.Chat;
import com.example.human_vs_zombies.enums.ChatScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("select count(c) from Chat c join Player p on c.player.player_id=p.player_id join Game g on p.game.game_id = g.game_id where g.game_id = ?1 and c.chatScope not like ?2")
    int countMessagesOfGame(int gameId, ChatScope scope);
}
