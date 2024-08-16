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
public class UserStyleResponse {

    @Schema(description = "등산 스타일 고유 번호", example = "1")
    private int hikingStylesId;

}
