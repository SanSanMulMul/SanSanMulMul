package com.sansantek.sansanmulmul.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserHikingStyleRequest {

    @Schema(description = "해당 회원 등산 스타일", example = "[1, 2, 3]")
    List<Integer> styles = new ArrayList<>();

}
