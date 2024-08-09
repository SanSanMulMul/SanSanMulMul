package com.sansantek.sansanmulmul.chat.service;


import com.sansantek.sansanmulmul.chat.domain.ChatMessage;
import com.sansantek.sansanmulmul.chat.repository.ChatMessageRepository;
import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> getChatMessages(int crewId) {
        return chatMessageRepository.findByCrew_CrewIdOrderByTimestampAsc(crewId);
    }

    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }
}