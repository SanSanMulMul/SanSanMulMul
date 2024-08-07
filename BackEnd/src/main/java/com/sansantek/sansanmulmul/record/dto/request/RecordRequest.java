package com.sansantek.sansanmulmul.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RecordRequest {
    private LocalDateTime recordStartTime;
    private long recordUpDistance;
    private long recordDownDistance;
    private long recordDuration;
    private long recordSteps;
    private double recordElevation;
    private long recordKcal;
}
