package com.sansantek.sansanmulmul.record.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
public class AllRecordResonse {

    @Schema(description = "기록 고유 번호", example = "1")
    private int recordId;
    @Schema(description = "그룹 고유 번호", example = "1")
    private int crewId;
    @Schema(description = "산 이름", example = "금오산")
    private String mountainName;
    @Schema(description = "실제 등산 출발 일시", example = "YYYY-MM-DD 23:59:59")
    private LocalDateTime recordStartTime;
    @Schema(description = "실제 등산 도착 일시", example = "YYYY-MM-DD 23:59:59")
    private LocalDateTime recordEndTime;
    @Schema(description = "산 이미지", example = "0")
    private String mountainImg;

}
