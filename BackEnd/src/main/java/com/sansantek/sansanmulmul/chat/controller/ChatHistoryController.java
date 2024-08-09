package com.sansantek.sansanmulmul.chat.controller;

import com.sansantek.sansanmulmul.chat.domain.ChatMessage;
import com.sansantek.sansanmulmul.chat.dto.ChatMessagesResponse;
import com.sansantek.sansanmulmul.chat.dto.UserMessageResponse;
import com.sansantek.sansanmulmul.chat.service.ChatService;
import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.service.CrewService;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ChatHistoryController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/chat/history/{crewId}")
    public List<ChatMessagesResponse> getChatHistory(@PathVariable int crewId) {
        List<ChatMessage> chatMessages = chatService.getChatMessages(crewId);
        return chatMessages.stream().map(chatMessage -> ChatMessagesResponse.builder()
                .messageContent(chatMessage.getMessageContent())
                .timestamp(chatMessage.getTimestamp())
                .user(UserMessageResponse.builder()
                        .userProviderId(chatMessage.getUser().getUserProviderId())
                        .userNickname(chatMessage.getUser().getUserNickname())
                        .userProfileImg(chatMessage.getUser().getUserProfileImg())
                        .build())
                .build()).collect(Collectors.toList());
    }
}
