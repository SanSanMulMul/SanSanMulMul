package com.sansantek.sansanmulmul.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class StoneResponse {
    private int stoneId;
    private String stoneName;
    private String stoneImg;
}
