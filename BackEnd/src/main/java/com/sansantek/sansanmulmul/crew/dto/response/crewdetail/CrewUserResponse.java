package com.sansantek.sansanmulmul.crew.dto.response.crewdetail;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrewUserResponse {

    /* 그룹 내에 참여중인 멤버들 목록 */
    // 방장여부 추가해야함
    private int userId;
    private String userName;
    private String userNickname;
    private String userGender;
    private String userProfileImg;
    private int userStaticBadge;
}
