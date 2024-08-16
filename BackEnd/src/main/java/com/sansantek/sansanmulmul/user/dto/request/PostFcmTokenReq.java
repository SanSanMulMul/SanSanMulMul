package com.sansantek.sansanmulmul.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class PostFcmTokenReq {
    @Schema(description = "FCM Token", example = "")
    private String fcmToken; //FCMtoken
}
