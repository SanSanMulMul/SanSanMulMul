package com.sansantek.sansanmulmul.crew.dto.response.crewdetail;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CrewUserResponse {

    /* 그룹 내에 참여중인 멤버들 목록(1번탭) */
    private int userId;
    private String userName;
    private String userNickname;
    private String userGender;
    private String userProfileImg;
    private int userStaticBadge;
    private boolean isLeader; // 현재 이 사용자가 이 그룹의 방장인지 여부
}
