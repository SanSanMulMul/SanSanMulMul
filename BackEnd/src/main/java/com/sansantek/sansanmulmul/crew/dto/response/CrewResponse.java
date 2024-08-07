package com.sansantek.sansanmulmul.crew.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

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
}
