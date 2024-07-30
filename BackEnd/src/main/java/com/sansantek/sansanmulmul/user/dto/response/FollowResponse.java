package com.sansantek.sansanmulmul.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class FollowResponse {
    // 사진, 칭호, 닉네임
    private String userProfileImg;
    private int userStaticBadge;
    private String userNickName;
}
