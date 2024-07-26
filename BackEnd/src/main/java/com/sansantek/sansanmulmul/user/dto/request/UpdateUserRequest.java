package com.sansantek.sansanmulmul.user.dto.request;

import com.sansantek.sansanmulmul.user.domain.GenderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
public class UpdateUserRequest {
    private String userNickName;
    private GenderStatus userGender;
    private String userProfileImg;
    private LocalDate userBirth;
    // 등산 스타일

    private long userTotalLength;
    private double userTotalElevation;
    private long userTotalSteps;
    private long userTotalKcal;
    private long userTotalHiking;
    private int userStoneCount;
}
