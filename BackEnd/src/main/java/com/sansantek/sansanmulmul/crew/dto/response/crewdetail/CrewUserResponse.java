package com.sansantek.sansanmulmul.crew.dto.response.crewdetail;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrewUserResponse {

    /* 그룹 내에 참여중인 멤버들 목록(1번탭) */
    // 방장여부 추가해야함
    private int userId;
    private String userName;
    private String userNickname;
    private String userGender;
    private String userProfileImg;
    private int userStaticBadge;
//    @Schema(description = "현재 사용자가 이 그룹의 방장인가", example = "false") // 방장인지 여부 true/false
//    private boolean isLeader;
}
