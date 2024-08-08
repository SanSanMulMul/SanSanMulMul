package com.sansantek.sansanmulmul.chat.controller;

import com.sansantek.sansanmulmul.chat.domain.ChatMessage;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Tag(name = "채팅 기능", description = "messageContent+timestamp+user(userProviderId,userNickname,userProfileImg)")
public class ChatController {

    private final ChatService chatService;
    private final CrewService crewService;
    private final UserService userService;


    @PostMapping("/{crewId}/messages")
    @Operation(summary = "메세지 작성", description = "메세지내용+시간+userProviderId +유저ProviderId,닉네임,프로필사진")
    public ResponseEntity<List<ChatMessagesResponse>> sendMessage(@PathVariable int crewId, @RequestBody String message, Authentication authentication) {
        String userProviderId = authentication.getName();

        // 사용자 그룹 가입 여부 확인
        if (!crewService.isUserInCrew(userProviderId, crewId)) {
            return ResponseEntity.status(403).build(); // 403 Forbidden
        }

        User user = userService.getUser(userProviderId);
        Crew crew = crewService.getCrewById(crewId);

        chatService.saveMessage(user, crew, message);
        List<ChatMessage> messages = chatService.getMessagesByCrew(crew);
        List<ChatMessagesResponse> messageResponses = messages.stream()
                .map(this::toChatMessageResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(messageResponses);
    }

    @GetMapping("/{crewId}/messages")
    @Operation(summary = "메세지 목록", description = "메세지내용+시간+userProviderId +유저ProviderId,닉네임,프로필사진")
    public ResponseEntity<List<ChatMessagesResponse>> getMessages(@PathVariable int crewId, Authentication authentication) {
        String userProviderId = authentication.getName();

        // 사용자 그룹 가입 여부 확인
        if (!crewService.isUserInCrew(userProviderId, crewId)) {
            return ResponseEntity.status(403).build(); // 403 Forbidden
        }

        Crew crew = crewService.getCrewById(crewId);
        List<ChatMessage> messages = chatService.getMessagesByCrew(crew);
        List<ChatMessagesResponse> messageResponses = messages.stream()
                .map(this::toChatMessageResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageResponses);
    }

    private ChatMessagesResponse toChatMessageResponse(ChatMessage chatMessage) {
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