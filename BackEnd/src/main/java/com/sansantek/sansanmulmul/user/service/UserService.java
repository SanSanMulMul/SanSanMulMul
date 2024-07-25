package com.sansantek.sansanmulmul.user.service;

import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.dto.request.SignUpUserRequest;
import com.sansantek.sansanmulmul.user.dto.request.UpdateUserRequest;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    // DB에 저장되어 있는 회원 확인
    public boolean isExistsUser(String userProviderId) {
        // true: 회원이 저장되어 있음
        // false: 회원이 저장되어 있지 않음
        return userRepository.existsByUserProviderId(userProviderId);
    }

    // DB에 저장되어 있는 회원 찾기
    public User getUser(String userProviderId) {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user;
    }

    // 회원가입 진행
    public User signUpUser(SignUpUserRequest signUpUserRequest) {
        User user = new User(
                signUpUserRequest.getUserProviderId(),
                signUpUserRequest.getUserName(),
                signUpUserRequest.getUserNickName(),
                signUpUserRequest.getUserGender(),
                signUpUserRequest.getUserProfileImg(),
                signUpUserRequest.getUserBirth()
//                signUpUserRequest.getUserTotalLength(),
//                signUpUserRequest.getUserTotalElevation(),
//                signUpUserRequest.getUserTotalSteps(),
//                signUpUserRequest.getUserTotalKcal(),
//                signUpUserRequest.getUserTotalHiking(),
//                signUpUserRequest.getUserStoneCount()
        );
        return userRepository.save(user);
    }

    // refreshToken 업데이트
    @Transactional
    public void saveRefreshToken(String userProviderId, String newRefreshToken) {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 기존 객체의 리프레시 토큰만 업데이트
        user.setUserRefreshToken(newRefreshToken);

        // 업데이트한 회원 저장
        userRepository.save(user);
    }

    // refreshToken 조회
    public String getRefreshToken(String userProviderId) {
        return userRepository.findUserRefreshTokenByUserProviderId(userProviderId).orElse(null);
    }

    // DB에 저장되어 있는 회원 닉네임 확인
    public boolean isExistsUserNickname(String userNickname) {
        // true: 회원 닉네임 중복
        // false: 회원이 닉네임 중복 X
        return userRepository.existsByUserNickname(userNickname);
    }

    // 회원 정보 수정
    public User updateUser(String userProviderId, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 업데이트할 정보 설정
        if (updateUserRequest.getUserNickName() != null) {
            user.setUserNickname(updateUserRequest.getUserNickName());
        }
        if (updateUserRequest.getUserGender() != null) {
            user.setUserGender(updateUserRequest.getUserGender());
        }
        if (updateUserRequest.getUserProfileImg() != null) {
            user.setUserProfileImg(updateUserRequest.getUserProfileImg());
        }
        if (updateUserRequest.getUserBirth() != null) {
            user.setUserBirth(updateUserRequest.getUserBirth());
        }

        // 기타 필드 업데이트
        user.setUserTotalLength(updateUserRequest.getUserTotalLength());
        user.setUserTotalElevation(updateUserRequest.getUserTotalElevation());
        user.setUserTotalSteps(updateUserRequest.getUserTotalSteps());
        user.setUserTotalKcal(updateUserRequest.getUserTotalKcal());
        user.setUserTotalHiking(updateUserRequest.getUserTotalHiking());
        user.setUserStoneCount(updateUserRequest.getUserStoneCount());

        // 저장 및 반환
        return userRepository.save(user);
    }
}
