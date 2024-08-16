package com.sansantek.sansanmulmul.record.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
public class LocationRequest {
    @Schema(description = "그룹 고유 번호", example = "1")
    private int crewId;
    @Schema(description = "사용자 위도", example = "123.123456")
    private Double userLat;
    @Schema(description = "사용자 경도", example = "123.123456")
    private Double userLon;
}
