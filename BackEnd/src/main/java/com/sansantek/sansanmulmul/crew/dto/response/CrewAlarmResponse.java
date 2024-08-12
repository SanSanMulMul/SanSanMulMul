package com.sansantek.sansanmulmul.crew.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CrewAlarmResponse {

    private int alarmId;
    private String alarmTitle;
    private String alarmBody;
    private LocalDateTime createdAt;

}
