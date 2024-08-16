package com.sansantek.sansanmulmul.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class UserMessageResponse {
    private String userProviderId;
    private String userNickname;
    private String userProfileImg;
}
