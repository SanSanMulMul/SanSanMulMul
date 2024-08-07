package com.sansantek.sansanmulmul.crew.dto.response.crewdetail;

import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyle;
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
public class CrewDetailResponse {

    /* 그룹 '그룹 정보'(1번탭) 상세보기 응답 */

    @Schema(description = "그룹 설명", example = "그룹설명부분입니다.")
    private String crewDescription;
    @Schema(description = "그룹 등산 스타일", example = "['설렁설렁', '등산도 식후경']")
    private List<String> crewHikingStyles = new ArrayList<>();



}
