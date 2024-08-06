package com.sansantek.sansanmulmul.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class MyPageResponse {

    @Schema(description = "회원 프로필 이미지", example = "http://t1.kakaocdn.net/account_images/default_profile.jpeg.twg.thumb.R640x640")
    private String userProfileImg;
    @Schema(description = "회원 칭호", example = "👶 등린이")
    private String userBadge;
    @Schema(description = "회원 닉네임", example = "김싸피")
    private String userNickname;
    @Schema(description = "회원 팔로워 수", example = "8")
    private int followerCnt;
    @Schema(description = "회원 팔로잉 수", example = "10")
    private int followingCnt;
    @Schema(description = "회원 등산 스타일", example = "설렁설렁")
    private List<String> styles = new ArrayList<>();

}
