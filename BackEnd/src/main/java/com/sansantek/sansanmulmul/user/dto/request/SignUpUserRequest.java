package com.sansantek.sansanmulmul.user.dto.request;

import com.sansantek.sansanmulmul.user.domain.GenderStatus;
import com.sansantek.sansanmulmul.user.domain.style.UserHikingStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SignUpUserRequest {
    private String userProviderId;
    private String userName;
    private String userNickName;
    private GenderStatus userGender;
    private String userProfileImg;
    private LocalDate userBirth;
    private boolean userIsAdmin;

    // 등산 스타일
    private List<Integer> userStyles = new ArrayList<>(); // 등산 스타일 id 저장

    // getter
    public boolean isUserIsAdmin() {
        return userIsAdmin;
    }
}
