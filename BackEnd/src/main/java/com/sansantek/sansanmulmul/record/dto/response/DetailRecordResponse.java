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
public class DetailRecordResponse {
    @Schema(description = "등산 출발 일시", example = "YYYY-MM-DD 23:59:59")
    private LocalDateTime recordStartTime;
    @Schema(description = "산 이름", example = "금오산")
    private String mountainName;
    @Schema(description = "산 이미지", example = "0")
    private String mountainImg;
//    @Schema(description = "상행 코스 이름", example = "금오산 제 1코스")
//    private String upCourseName;
//    @Schema(description = "하행 코스 이름", example = "금오산 제 1코스")
//    private String downCourseName;
    // 코스 좌표
    // 참여 멤버
    @Schema(description = "등산 걸음 수(단위: 걸음)", example = "0")
    private long recordSteps;
    @Schema(description = "등산 고도(단위: m)", example = "0")
    private double recordElevation;
    @Schema(description = "등산 소요 칼로리(단위: Kcal)", example = "0")
    private int recordKcal;
}
