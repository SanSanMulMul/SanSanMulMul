package com.sansantek.sansanmulmul.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class KakaoLogin {

    public String accessToken;
    public String refreshToken;
}
