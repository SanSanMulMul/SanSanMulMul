package com.sansantek.sansanmulmul.record.dto.response;

import com.sansantek.sansanmulmul.crew.dto.response.crewdetail.CrewUserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DetailRecordResponse {
    
    // 산
    @Schema(description = "산 이름", example = "금오산")
    private String mountainName;
    @Schema(description = "산 이미지", example = "0")
    private String mountainImg;
    
    // 코스(코스 이름, 코스 길이, 코스 좌표들)
    @Schema(description = "상행 코스 이름", example = "금오산제1코스")
    private String upCourseName;
    @Schema(description = "하행 코스 이름", example = "금오산제3코스")
    private String downCourseName;
    @Schema(description = "상행 코스 길이(단위: m)", example = "1.23")
    private double upCourseLength;
    @Schema(description = "하행 코스 길이(단위: m)", example = "1.23")
    private double downCourseLength;
    @Schema(description = "상행 코스 좌표", example = "trackpaths(고유번호, 위도, 경도)")
    private List<Map<String, Object>>  upCourseTrackPaths = new ArrayList<>(); // TrackPathRepository 써야함
    @Schema(description = "하행 코스 좌표", example = "trackpaths(고유번호, 위도, 경도)")
    private List<Map<String, Object>>  downCourseTrackPaths = new ArrayList<>();

    // 참여 멤버(프로필, 닉네임)
    @Schema(description = "등산 참여 멤버 정보", example = "members(id, 이름, 닉네임, 성별, 프로필, 칭호)")
    List<CrewUserResponse> crewMembers = new ArrayList<>(); // CrewUserRepository

    // 기록
    @Schema(description = "실제 등산 출발 일시", example = "YYYY-MM-DD 23:59:59")
    private LocalDateTime recordStartTime;
    @Schema(description = "실제 등산 도착 일시", example = "YYYY-MM-DD 23:59:59")
    private LocalDateTime recordEndTime;
    @Schema(description = "등산 전체 이동 거리(단위: m)", example = "0.0")
    private long recordDistance;
    @Schema(description = "등산 산행 소요 시간(단위: 분)", example = "0")
    private long recordDuration;
    @Schema(description = "등산 걸음 수(단위: 걸음)", example = "0")
    private long recordSteps;
    @Schema(description = "등산 고도(단위: m)", example = "0")
    private double recordElevation;
    @Schema(description = "등산 소요 칼로리(단위: Kcal)", example = "0")
    private int recordKcal;
}
