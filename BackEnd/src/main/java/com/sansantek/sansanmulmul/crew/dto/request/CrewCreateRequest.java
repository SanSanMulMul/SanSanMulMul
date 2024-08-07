package com.sansantek.sansanmulmul.crew.dto.request;

import com.sansantek.sansanmulmul.crew.domain.CrewRestriction;
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
public class CrewCreateRequest {

    /* 그룹 생성 요청 */
    @Schema(description = "그룹 이름", example = "한사랑 산악회")
    private String crewName;
    @Schema(description = "그룹 설명", example = "모집합니다.")
    private String crewDescription;
    @Schema(description = "그룹 참여 최대 인원", example = "10")
    private int crewMaxMembers;
    @Schema(description = "그룹 허용 성별", example = "A")
    private CrewRestriction crewGender;
    @Schema(description = "그룹 참여 최소 연령", example = "10")
    private int crewMinAge;
    @Schema(description = "그룹 참여 최대 연령", example = "90")
    private int crewMaxAge;
    @Schema(description = "그룹 시작 일시", example = "2024-08-01 23:59:59")
    private int crewStartDate;
    @Schema(description = "그룹 종료 일시", example = "2024-08-02 23:59:59")
    private int crewEndDate;
    @Schema(description = "그룹 등산 스타일", example = "[1, 2, 3]")
    private List<Integer> crewStyles = new ArrayList<>();

    // <산 선택>
    @Schema(description = "산 고유 번호", example = "14")
    private int mountainId;

    // <코스 선택>
    // 상행 코스
    @Schema(description = "상행 코스 고유 번호", example = "47190010101")
    private int upCourseId;
    // 하행 코스
    @Schema(description = "하행 코스 고유 번호", example = "47190010102")
    private int downCourseId;


}
