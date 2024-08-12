package com.sansantek.sansanmulmul.mountain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class MountainResponse {
    @Schema(description = "산 고유 번호", example = "1")
    private int mountainId;

    @Schema(description = "산 고유 코드", example = "4130612")
    private int mountainCode;

    @Schema(description = "산 이름", example = "금오산")
    private String mountainName;

    @Schema(description = "산 지역", example = "경상북도 구미시")
    private String mountainLocation;

    @Schema(description = "산 높이", example = "1257")
    private int mountainHeight;

    @Schema(description = "산 상세 설명", example = "금오산은 구미를 대표하는 산중하나이다.")
    private String mountainDescription;

    @Schema(description = "산 이미지", example = "0")
    private String mountainImg;

    @Schema(description = "계절명산" ,example = "SUMMER")
    private String mountainWeather;

    @Schema(description = "산 위도",example = "35.405006")
    private double mountainLat; //위도

    @Schema(description = "산 경도", example = "124.123456")
    private double mountainLon; //경도
}
