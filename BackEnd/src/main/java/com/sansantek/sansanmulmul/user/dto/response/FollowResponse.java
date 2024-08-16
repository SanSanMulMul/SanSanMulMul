package com.sansantek.sansanmulmul.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class FollowResponse {
    @Schema(description = "회원 고유 번호", example = "1")
    private int userId;
    @Schema(description = "회원 프로필 이미지", example = "http://t1.kakaocdn.net/account_images/default_profile.jpeg.twg.thumb.R640x640")
    private String userProfileImg;
    @Schema(description = "회원 칭호", example = "👶 등린이")
    private String userBadge;
    @Schema(description = "회원 닉네임", example = "김싸피")
    private String userNickName;
}
