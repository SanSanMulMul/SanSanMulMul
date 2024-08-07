package com.sansantek.sansanmulmul.crew.dto.response.crewdetail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class CrewHikingDetailResponse {

    /* 그룹 '등산 정보' 상세보기 응답 */
    //공통
    // 그룹 시작, 그룹 끝, 그룹 이름, [참여중 인원], 그룹 최대인원
    // 방장인지 여부 true/false도 **
    // 그룹 설명, 그룹hikingstyles 'name'으로,

    ///
    // <'등산 정보'>
    // 산 (idㄴㄴ)이름 , 산 설명
    // 상행코스 - id,  name, 난이도, 상행 소요시간, 거리
    // 하행코스 - id, name, 난이도, 하행 소요시간, 거리
    // 산 정보
    @Schema(description = "산 고유 번호", example = "1")
    private int mountainId;
    @Schema(description = "산 이름", example = "금오산")
    private String mountainName;
    @Schema(description = "산 상세 설명", example = "금오산은 구미를 대표하는 산중하나이다.")
    private String mountainDescription;
    @Schema(description = "산 이미지", example = "0")
    private String mountainImg;
    // 코스

}
