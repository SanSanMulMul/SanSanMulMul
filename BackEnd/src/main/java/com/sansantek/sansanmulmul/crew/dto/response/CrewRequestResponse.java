package com.sansantek.sansanmulmul.crew.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrewRequestResponse {

    /* 그룹 승인 요청 응답 */
    private int requestId;
    private String userName;
    private String userNickname;
    private String requestStatus;
    private String userGender;
    private String userProfileImg;
    private int userStaticBadge;
}
