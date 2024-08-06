package com.sansantek.sansanmulmul.mountain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class NewsResponse {
    @Schema(name = "산 이름", example = "가리산")
    private String mountainName;
    @Schema(name = "산 이미지", example = "1.png")
    private String mountainImg;
}
