package com.sansantek.sansanmulmul.crew.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrewRequestResponse {

    private int requestId;
    private String userName;
    private String userNickname;
    private String requestStatus;
}
