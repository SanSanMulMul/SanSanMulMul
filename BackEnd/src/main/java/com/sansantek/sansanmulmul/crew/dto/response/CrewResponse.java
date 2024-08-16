package com.sansantek.sansanmulmul.crew.dto.response;

import com.sansantek.sansanmulmul.crew.domain.CrewRestriction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class CrewResponse {

    /* 그룹 전체 목록 조회 시 응답 */
    @Schema(description = "그룹 고유 번호", example = "1")
    private int crewId;
    @Schema(description = "그룹 이름", example = "한사랑 산악회")
    private String crewName;
    @Schema(description = "산 이름", example = "금오산")
    private String mountainName;
    @Schema(description = "상행 코스 이름", example = "금오산 제 1코스")
    private String upCourseName;
    @Schema(description = "하행 코스 이름", example = "금오산 제 2코스")
    private String downCourseName;
    @Schema(description = "그룹 시작 일시", example = "2024-08-01 22:59:59")
    private LocalDateTime crewStartDate;
    @Schema(description = "그룹 종료 일시", example = "2024-08-01 23:59:59")
    private LocalDateTime crewEndDate;
    @Schema(description = "그룹 참여 최대 인원", example = "10")
    private int crewMaxMembers;
    @Schema(description = "그룹에 현재 참여 중인 인원", example = "3")
    private int crewCurrentMembers;
    @Schema(description = "현재 사용자가 이 그룹에 참여된 상태인가", example = "false")
    private boolean isUserJoined;
    @Schema(description = "산 이미지 경로", example = "1")
    private String mountainImg;
    //최소, 최대나이, 성별, 스타일
    @Schema(description = "그룹 참여 최소 연령", example = "10")
    private int crewMinAge;
    @Schema(description = "그룹 참여 최대 연령", example = "90")
    private int crewMaxAge;
    @Schema(description = "그룹 허용 성별", example = "A")
    private CrewRestriction crewGender;
    @Schema(description = "그룹 등산 스타일", example = "[2, 4, 5]")
    private List<Integer> crewStyles = new ArrayList<>();
    // 방장 정보 - 프로필사진, 닉네임, 칭호
    private int userStaticBadge;
    private String userNickname;
    private String userProfileImg;
}
