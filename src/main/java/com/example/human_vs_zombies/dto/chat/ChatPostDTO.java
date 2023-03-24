package com.example.human_vs_zombies.dto.chat;

import com.example.human_vs_zombies.enums.ChatScope;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatPostDTO {
    private String message;
    private ChatScope chatScope;
    private int player;
    private int squad;
}
