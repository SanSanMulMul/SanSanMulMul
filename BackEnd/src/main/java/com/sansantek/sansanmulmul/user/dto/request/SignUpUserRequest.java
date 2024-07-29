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
public class SignUpUserRequest {
    private String userProviderId;
    private String userName;
    private String userNickName;
    private GenderStatus userGender;
    private String userProfileImg; // nullable
    private LocalDate userBirth;
    // 등산 스타일

}
