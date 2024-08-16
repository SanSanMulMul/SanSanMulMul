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
public class CrewDetailCommonResponse {

    /* 그룹 상세보기 시, 상단에 고정된 [공통 부분] 응답 */
    @Schema(description = "그룹 고유 번호", example = "1")
    private int crewId;
    @Schema(description = "그룹 이름", example = "한사랑 산악회")
    private String crewName;
    @Schema(description = "그룹 시작 일시", example = "2024-08-01 23:59:59")
    private LocalDateTime crewStartDate;
    @Schema(description = "그룹 종료 일시", example = "2024-08-02 23:59:59")
    private LocalDateTime crewEndDate;
    @Schema(description = "그룹에 현재 참여 중인 인원", example = "3")    //참여중 인원
    private int crewCurrentMembers;
    @Schema(description = "그룹 참여 최대 인원", example = "10")
    private int crewMaxMembers;
    @Schema(description = "산 이미지", example = "0")
    private String mountainImg;
    @Schema(description = "현재 사용자가 이 그룹의 방장인가", example = "false") // 방장인지 여부 true/false
    private boolean isLeader;





}
