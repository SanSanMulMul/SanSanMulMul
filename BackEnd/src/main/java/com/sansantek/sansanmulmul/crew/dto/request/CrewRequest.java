package com.sansantek.sansanmulmul.crew.dto.request;

import com.sansantek.sansanmulmul.crew.domain.CrewRestriction;
import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyle;
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
public class CrewRequest {
    // 그룹
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
    @Schema(description = "그룹 등산 스타일", example = "[1, 2, 3]")
    private List<CrewHikingStyle> crewStyles = new ArrayList<>();

    // 산
    @Schema(description = "산 고유 번호", example = "1")
    private int mountainId;

    // 코스
    // 상행 코스
    // 하행 코스
}
