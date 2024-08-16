package com.sansantek.sansanmulmul.common.util;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.sansantek.sansanmulmul.user.domain.User;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.sansantek.sansanmulmul.common.util.FcmMessage.FcmDTO;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class FcmUtil {

    @Async("taskExecutor")
    public void singleFcmSend(User user, FcmDTO fcmDTO) {
        String fcmToken = user.getFcmToken();

        if (fcmToken != null && !fcmToken.isEmpty()) {
            Message message = makeMessage(fcmDTO.getTitle(), fcmDTO.getBody(), fcmToken);
            sendMessage(message);
        }
    }

    @Async("taskExecutor")
    public void multiFcmSend(List<User> users, FcmDTO fcmDTO) {
        users.forEach(user -> singleFcmSend(user, fcmDTO));
    }


    public Message makeMessage(String title, String body, String token) { //FcmDTO title, body사용
        Notification notification =
                Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build();

        return Message.builder()
                .setNotification(notification)
                .setToken(token)
                .putData("title", title)
                .putData("body", body)
                .build();
    }

    public void sendMessage(Message message) {
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("FCM send error");
        }
    }

    public FcmDTO makeFcmDTO(String title, String body) {
        return FcmDTO.builder()
                .title(title)
                .body(body)
                .build();
    }

    ////
    public String makeFcmTitle(String crewName, String type) {
        // return crewName + " " + type;
        return type;
    }

    //
    
    public String makeNoticeBody(String crewName, String type) {
        return crewName + " " + type + "가 등록되었어요.";
    }

    public String makeMessageBody(String crewName, String sendUserName, String message) {
        return "["+crewName+"] " + sendUserName + " : " + message;
    }

    public String makeJoinRequestBody(String requestUserName, String crewName) {
        return requestUserName + "님이 [" + crewName + "] 그룹에 가입신청 하였습니다.";
    }

    public String makeLeaderDelegateBody(String crewName, String currentLeaderName, String newLeaderName) {
        return "["+crewName+"] 방장이 " + currentLeaderName + "님에서 " + newLeaderName + "님으로 변경되었습니다.";
    }

}
