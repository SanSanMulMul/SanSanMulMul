package com.sansantek.sansanmulmul.chat.service;


import com.sansantek.sansanmulmul.chat.domain.ChatMessage;
import com.sansantek.sansanmulmul.chat.repository.ChatMessageRepository;
import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(User user, Crew crew, String messageContent) {
        ChatMessage chatMessage = ChatMessage.builder()
                .user(user)
                .crew(crew)
                .messageContent(messageContent)
                .timestamp(LocalDateTime.now())
                .build();
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getMessagesByCrew(Crew crew) {
        return chatMessageRepository.findByCrewOrderByTimestamp(crew);
    }
}
