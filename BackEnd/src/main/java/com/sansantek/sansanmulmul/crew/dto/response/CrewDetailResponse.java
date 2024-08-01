package com.sansantek.sansanmulmul.crew.dto.response;

import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class CrewDetailResponse {

    // 그룹
    @Schema(description = "그룹 고유 번호", example = "1")
    private int crewId;
    @Schema(description = "그룹 이름", example = "한사랑 산악회")
    private String crewName;
    @Schema(description = "그룹 시작 일시", example = "2024-08-01 23:59:59")
    private LocalDateTime crewStartDate;
    @Schema(description = "그룹 종료 일시", example = "2024-08-02 23:59:59")
    private LocalDateTime crewEndDate;
    @Schema(description = "그룹 설명", example = "모집합니다.")
    private String crewDescription;
    @Schema(description = "그룹 등산 스타일", example = "[1, 2, 3]")
    private List<CrewHikingStyle> style;
    @Schema(description = "그룹 참여 최대 인원", example = "10")
    private int crewMaxMembers;

    // 멤버
    // 참여 멤버 수
    // 방장 정보
    // 멤버 목록

    // 산 정보
    @Schema(description = "산 고유 번호", example = "1")
    private int mountainId;
    @Schema(description = "산 이름", example = "금오산")
    private String mountainName;
    @Schema(description = "산 상세 설명", example = "금오산은 구미를 대표하는 산중하나이다.")
    private String mountainDescription;
    @Schema(description = "산 이미지", example = "0")
    private String mountainImg;
    // 코스
}
