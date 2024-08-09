package com.sansantek.sansanmulmul.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChatMessageDTO {
    private String messageContent;
    private LocalDateTime timestamp;
    private int crewId;
    private int userId;
}