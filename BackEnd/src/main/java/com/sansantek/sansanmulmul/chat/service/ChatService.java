package com.sansantek.sansanmulmul.chat.service;


import com.sansantek.sansanmulmul.chat.domain.ChatMessage;
import com.sansantek.sansanmulmul.chat.repository.ChatMessageRepository;
import com.sansantek.sansanmulmul.common.util.FcmMessage;
import com.sansantek.sansanmulmul.common.util.FcmType;
import com.sansantek.sansanmulmul.common.util.FcmUtil;
import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.crewuser.CrewUser;
import com.sansantek.sansanmulmul.crew.repository.request.CrewUserRepository;
import com.sansantek.sansanmulmul.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final CrewUserRepository crewUserRepository;
    private final FcmUtil fcmUtil;

    public List<ChatMessage> getChatMessages(int crewId) {
        return chatMessageRepository.findByCrew_CrewIdOrderByTimestampAsc(crewId);
    }

    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public void sendFCMnotification(Crew crew, User user, String msgContent) {
        // FcmDTO 생성
        String title = fcmUtil.makeFcmTitle(
                crew.getCrewName(), FcmType.MESSAGE.getType()
        );
        String body = fcmUtil.makeMessageBody(
                crew.getCrewName(), user.getUserName(), msgContent
        );
        FcmMessage.FcmDTO fcmDTO = fcmUtil.makeFcmDTO(title, body);
        // FCM발송
        fcmSendtoCrew(crew, user, fcmDTO);
    }

    // 그룹 내 회원들에게 전체 알림
    public void fcmSendtoCrew(Crew crew, User sendUser, FcmMessage.FcmDTO fcmDTO){

        List<CrewUser> crewUsers = crewUserRepository.findByCrew(crew);// 그룹 내 속하는 멤버들


        fcmUtil.multiFcmSend(
                crewUsers.stream()
                        .map(CrewUser::getUser)
                        .filter(user -> !user.equals(sendUser)) // sendUser와 같지 않은 유저만 필터링
                        .toList(),
                fcmDTO
        );
    }
}