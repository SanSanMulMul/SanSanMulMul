package com.sansantek.sansanmulmul.user.dto.request;

import com.sansantek.sansanmulmul.user.domain.GenderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SignUpUserRequest {
    private String userProviderId;
    private String userPassword;
    private String userName;
    private String userNickName;
    private GenderStatus userGender;
    private String userProfileImg; // nullable
    private LocalDate userBirth;
    private boolean userIsAdmin;

    public boolean isUserIsAdmin() {
        return userIsAdmin;
    }

    // 등산 스타일

}
