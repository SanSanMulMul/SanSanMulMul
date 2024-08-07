package com.sansantek.sansanmulmul.chat.dto;

import com.sansantek.sansanmulmul.chat.domain.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChatMessagesResponse {
    private String messageContent;
    private LocalDateTime timestamp;
    private UserMessageResponse user;
}





