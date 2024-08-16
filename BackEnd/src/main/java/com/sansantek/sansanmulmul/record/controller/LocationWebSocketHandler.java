package com.sansantek.sansanmulmul.record.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LocationWebSocketHandler {

    @MessageMapping("/location")
    @SendTo("/topic/group/{groupId}")
    public ResponseEntity<?> handleLocationUpdate(@DestinationVariable String groupId) {
        // 위치 업데이트 처리 및 거리 계산 로직
        HttpStatus status = HttpStatus.ACCEPTED;


        return new ResponseEntity<>(status);
    }
}