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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;


@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessageDTO chatMessageDTO) {
        User user = userService.getUser(chatMessageDTO.getUserId());
        Crew crew = Crew.builder().crewId(chatMessageDTO.getCrewId()).build();
        ChatMessage chatMessage = ChatMessage.builder()
                .messageContent(chatMessageDTO.getMessageContent())
                .timestamp(LocalDateTime.now())
                .crew(crew)
                .user(user)
                .build();

        chatService.saveChatMessage(chatMessage);

        chatService.sendFCMnotification(crew, user, chatMessage.getMessageContent());

        ChatMessagesResponse response = ChatMessagesResponse.builder()
                .messageContent(chatMessage.getMessageContent())
                .timestamp(chatMessage.getTimestamp())
                .user(UserMessageResponse.builder()
                        .userProviderId(chatMessage.getUser().getUserProviderId())
                        .userNickname(chatMessage.getUser().getUserNickname())
                        .userProfileImg(chatMessage.getUser().getUserProfileImg())
                        .build())
                .build();

        messagingTemplate.convertAndSend("/topic/public/" + chatMessageDTO.getCrewId(), response);
    }
}