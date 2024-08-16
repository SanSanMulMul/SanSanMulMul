package com.sansantek.sansanmulmul.crew.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class CrewUpdateRequest {

    /* 그룹 일정 수정 */
    @Schema(description = "그룹 고유 번호", example = "1")
    private int crewId;
    // 일정?
    @Schema(description = "그룹 시작 일시", example = "2024-08-01 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime crewStartDate;
    @Schema(description = "그룹 종료 일시", example = "2024-08-02 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime crewEndDate;
    // 산
    @Schema(description = "산 고유 번호", example = "14")
    private int mountainId;
    // 코스 - 상행
    @Schema(description = "상행 코스 고유 번호", example = "47190010101")
    private Long upCourseId;
    // 코스 - 하행
    @Schema(description = "하행 코스 고유 번호", example = "47190010102")
    private Long downCourseId;


}
