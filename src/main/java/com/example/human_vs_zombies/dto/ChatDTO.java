package com.example.human_vs_zombies.dto;

import com.example.human_vs_zombies.enums.ChatScope;
import lombok.Data;

@Data
public class ChatDTO {

    private int message_id;
    private String message;
    private ChatScope chatScope;
    private int player;
    private int squad;
}
