package com.sansantek.sansanmulmul.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class StoneResponse {

    @Schema(description = "정상석 고유 번호", example = "14")
    private int stoneId;
    @Schema(description = "산 이름", example = "금오산")
    private String mountainName;
    @Schema(description = "정상석 이름", example = "현월봉")
    private String stoneName;
    @Schema(description = "정상석 이미지", example = "Img")
    private String stoneImg;

}
