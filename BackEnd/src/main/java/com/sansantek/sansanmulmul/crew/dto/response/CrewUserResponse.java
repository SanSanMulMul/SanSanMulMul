package com.sansantek.sansanmulmul.crew.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrewUserResponse {
    private int userId;
    private String userName;
    private String userNickname;
    private String userGender;
    private String userProfileImg;
    private int userStaticBadge;
}
