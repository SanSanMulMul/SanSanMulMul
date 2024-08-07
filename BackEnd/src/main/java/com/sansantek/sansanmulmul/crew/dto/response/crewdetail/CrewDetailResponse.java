package com.sansantek.sansanmulmul.crew.dto.response.crewdetail;

import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyle;
import io.swagger.v3.oas.annotations.media.Schema;
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

    /* 그룹 '그룹 정보' 상세보기 응답 */
    //공통
    // 그룹 시작시간, 그룹 끝, 그룹 이름, [참여중 인원], 그룹 최대인원
    // 방장인지 여부 true/false도 **
    // 그룹 설명, 그룹hikingstyles 'name'으로,

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


}
