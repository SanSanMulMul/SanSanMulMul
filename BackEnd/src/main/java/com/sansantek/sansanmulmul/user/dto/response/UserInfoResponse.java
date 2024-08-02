package com.sansantek.sansanmulmul.user.dto.response;

import com.sansantek.sansanmulmul.user.domain.GenderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponse {

    @Schema(description = "회원 고유 번호", example = "1")
    private int userId;
    @Schema(description = "회원 카카오 아이디", example = "33213515")
    private String userProviderId;
    @Schema(description = "회원 이름", example = "김싸피")
    private String userName;
    @Schema(description = "회원 닉네임", example = "싸린이")
    private String userNickName;
    @Schema(description = "회원 성별", example = "M")
    private GenderStatus userGender;
    @Schema(description = "회원 카카오 프로필 이미지", example = "http://t1.kakaocdn.net/account_images/default_profile.jpeg.twg.thumb.R640x640")
    private String userProfileImg;
    @Schema(description = "회원 생일", example = "2024-08-01")
    private LocalDate userBirth;
    @Schema(description = "회원 고정 칭호", example = "1")
    private int userStaticBadge;
    @Schema(description = "회원 누적 이동거리", example = "0.0")
    private double userTotalLength;
    @Schema(description = "회원 누적 고도", example = "0.0")
    private double userTotalElevation;
    @Schema(description = "회원 누적 걸음수", example = "0")
    private long userTotalSteps;
    @Schema(description = "회원 누적 소모 칼로리", example = "0")
    private long userTotalKcal;
    @Schema(description = "회원 누적 산행 횟수", example = "0")
    private long userTotalHiking;
    @Schema(description = "회원 획득 비석 개수", example = "0")
    private int userStoneCount;
    @Schema(description = "회원 관리자 유무", example = "false")
    private boolean userIsAdmin;

}
