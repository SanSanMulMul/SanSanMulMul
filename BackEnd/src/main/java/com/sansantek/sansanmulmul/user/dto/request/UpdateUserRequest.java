package com.sansantek.sansanmulmul.user.dto.request;

import com.sansantek.sansanmulmul.user.domain.GenderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class UpdateUserRequest {
    @Schema(description = "회원 프로필 이미지", example = "http://t1.kakaocdn.net/account_images/default_profile.jpeg.twg.thumb.R640x640")
    private String userProfileImg;
    @Schema(description = "회원 닉네임", example = "싸린이")
    private String userNickName;
    @Schema(description = "회원 고정 칭호", example = "1")
    private int userStaticBadge;
    @Schema(description = "회원 등산 스타일", example = "[1, 2, 3]")
    private List<Integer> styles = new ArrayList<>();
}
