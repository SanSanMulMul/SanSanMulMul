package com.sansantek.sansanmulmul.crew.dto.response.crewdetail;

import com.sansantek.sansanmulmul.mountain.domain.course.Level;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class CrewHikingDetailResponse {

    /* 그룹 '등산 정보'(2번탭) 상세보기 응답 */
    // 산 id, 이름 , 산 설명
    // 상행코스 - id,  name, 난이도, 상행 소요시간, 코스길이
    // 하행코스 - id, name, 난이도, 하행 소요시간, 코스길이

    // 산
    @Schema(description = "산 고유 번호", example = "1")
    private int mountainId;
    @Schema(description = "산 이름", example = "금오산")
    private String mountainName;
    @Schema(description = "산 상세 설명", example = "금오산은 구미를 대표하는 산중하나이다.")
    private String mountainDescription;
    @Schema(description = "산 위도",example = "35.405006")
    private double mountainLat; //위도
    @Schema(description = "산 경도", example = "124.123456")
    private double mountainLon; //경도

    // 코스 - 상행
    @Schema(description = "상행 코스 고유 번호" , example = "47190010101")
    private Long upCourseId;
    @Schema(description = "상행 코스 이름", example = "금오산 제 1코스")
    private String upCourseName;
    @Schema(description = "상행 코스 난이도", example = "HARD")
    private Level upCourseLevel;
    @Schema(description = "상행 소요시간",example = "155")
    private int upCoursetime; //courseUpTime 사용
    @Schema(description = "상행 코스길이", example = "8.85")
    private double upCourseLength; //courseLength 사용

    // 코스 - 하행
    @Schema(description = "하행 코스 고유 번호" , example = "47190010103")
    private Long downCourseId;
    @Schema(description = "하행 코스 이름", example = "금오산 제 3코스")
    private String downCourseName;
    @Schema(description = "하행 코스 난이도", example = "EASY")
    private Level downCourseLevel;
    @Schema(description = "하행 소요시간",example = "44")
    private int downCoursetime; //courseDownTime 사용
    @Schema(description = "하행 코스길이", example = "3.58")
    private double downCourseLength; //courseLength 사용

    // 상행, 하행 코스 좌표
    @Schema(description = "상행 코스 좌표", example = "trackpaths(고유번호, 위도, 경도)")
    private List<Map<String, Object>> upCourseTrackPaths = new ArrayList<>(); // TrackPathRepository 써야함
    @Schema(description = "하행 코스 좌표", example = "trackpaths(고유번호, 위도, 경도)")
    private List<Map<String, Object>>  downCourseTrackPaths = new ArrayList<>();

}
