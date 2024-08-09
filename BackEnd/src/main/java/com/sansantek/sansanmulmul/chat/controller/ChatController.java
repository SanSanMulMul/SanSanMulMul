package com.sansantek.sansanmulmul.chat.controller;

import com.sansantek.sansanmulmul.chat.domain.ChatMessage;
import com.sansantek.sansanmulmul.chat.dto.ChatMessageDTO;
import com.sansantek.sansanmulmul.chat.dto.ChatMessagesResponse;
import com.sansantek.sansanmulmul.chat.dto.UserMessageResponse;
import com.sansantek.sansanmulmul.chat.service.ChatService;
import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.service.CrewService;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessagesResponse sendMessage(ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = ChatMessage.builder()
                .messageContent(chatMessageDTO.getMessageContent())
                .timestamp(LocalDateTime.now())
                .crew(Crew.builder().crewId(chatMessageDTO.getCrewId()).build())
                .user(User.builder().userId(chatMessageDTO.getUserId()).build())
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

