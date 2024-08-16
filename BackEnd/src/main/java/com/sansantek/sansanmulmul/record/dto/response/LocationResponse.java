package com.sansantek.sansanmulmul.record.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
public class LocationResponse {
    @Schema(description = "회원 고유 번호", example = "1")
    private int userId;
    @Schema(description = "회원 닉네임", example = "싸린이")
    private String userNickname;
    @Schema(description = "회원 프로필 이미지", example = "http://t1.kakaocdn.net/account_images/default_profile.jpeg.twg.thumb.R640x640")
    private String userProfileImg;
    @Schema(description = "사용자 위도", example = "123.123456")
    private Double userLat;
    @Schema(description = "사용자 경도", example = "123.123456")
    private Double userLon;
}
