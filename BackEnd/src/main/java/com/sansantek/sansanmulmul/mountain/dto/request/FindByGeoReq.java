package com.sansantek.sansanmulmul.mountain.dto.request;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class FindByGeoReq {

    //현재 내 중심좌표
    @Schema(description = "내 현재 위치 위도", example = "36.1073")
    private Double latitude;
    @Schema(description = "내 현재 위치 경도", example = "128.417")
    private Double longitude;
    @Schema(description = "현재 위치 밴경 n km 내", example = "50")
    private Double radius; //반경 radius km내

}
