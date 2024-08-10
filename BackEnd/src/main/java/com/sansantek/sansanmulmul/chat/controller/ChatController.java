package com.sansantek.sansanmulmul.chat.controller;

import com.sansantek.sansanmulmul.chat.domain.ChatMessage;
import com.sansantek.sansanmulmul.chat.dto.ChatMessageDTO;
import com.sansantek.sansanmulmul.chat.dto.ChatMessagesResponse;
import com.sansantek.sansanmulmul.chat.dto.UserMessageResponse;
import com.sansantek.sansanmulmul.chat.service.ChatService;
import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;


@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessagesResponse sendMessage(ChatMessageDTO chatMessageDTO) {
        User user = userService.getUser(chatMessageDTO.getUserId());
        ChatMessage chatMessage = ChatMessage.builder()
                .messageContent(chatMessageDTO.getMessageContent())
                .timestamp(LocalDateTime.now())
                .crew(Crew.builder().crewId(chatMessageDTO.getCrewId()).build())
                .user(user)
                .build();

        chatService.saveChatMessage(chatMessage);

        return ChatMessagesResponse.builder()
                .messageContent(chatMessage.getMessageContent())
                .timestamp(chatMessage.getTimestamp())
                .user(UserMessageResponse.builder()
                        .userProviderId(chatMessage.getUser().getUserProviderId())
                        .userNickname(chatMessage.getUser().getUserNickname())
                        .userProfileImg(chatMessage.getUser().getUserProfileImg())
                        .build())
                .build();
    }
}

