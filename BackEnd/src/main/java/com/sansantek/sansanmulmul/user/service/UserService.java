package com.sansantek.sansanmulmul.user.service;

import com.sansantek.sansanmulmul.exception.auth.UserNotFoundException;
import com.sansantek.sansanmulmul.exception.user.UserDeletionException;
import com.sansantek.sansanmulmul.exception.user.UserUpdateException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.dto.request.SignUpUserRequest;
import com.sansantek.sansanmulmul.user.dto.request.UpdateUserHikingStyleRequest;
import com.sansantek.sansanmulmul.user.dto.request.UpdateUserRequest;
import com.sansantek.sansanmulmul.user.dto.response.UserInfoResponse;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import com.sansantek.sansanmulmul.user.service.style.UserStyleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    // Spring Security
    private final PasswordEncoder passwordEncoder;

    // Repository
    private final UserRepository userRepository;

    // Service
    private final UserStyleService userStyleService;

    @Transactional
    public User signUp(SignUpUserRequest signUpUserRequest, String password) {
        // 비밀번호 인코딩
        String encoder = passwordEncoder.encode(password);

        // 회원 객체 생성
        User user = new User(
                signUpUserRequest.getUserProviderId(),
                encoder,
                signUpUserRequest.getUserName(),
                signUpUserRequest.getUserNickName(),
                signUpUserRequest.getUserGender(),
                signUpUserRequest.getUserProfileImg(),
                signUpUserRequest.getUserBirth(),
                1,
                signUpUserRequest.isUserIsAdmin()
        );

        // 회원 영속성
        userRepository.save(user);

        return user;
    }

    public boolean isExistsUser(String userProviderId) {
        return userRepository.existsByUserProviderId(userProviderId);
    }

    public boolean isExistsUserNickname(String userNickname) {
        return userRepository.existsByUserNickname(userNickname);
    }

    public User getUser(String userProviderId) {
        return userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getUser(int userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public UserInfoResponse getUserResponse(String userProviderId) {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserInfoResponse userInfo = new UserInfoResponse(
                user.getUserId(),
                user.getUserProviderId(),
                user.getUserName(),
                user.getUserNickname(),
                user.getUserGender(),
                user.getUserProfileImg(),
                user.getUserBirth(),
                user.getUserStaticBadge(),
                user.getUserTotalLength(),
                user.getUserTotalElevation(),
                user.getUserTotalSteps(),
                user.getUserTotalKcal(),
                user.getUserTotalHiking(),
                user.getUserStoneCount(),
                user.isUserIsAdmin()
        );

        return userInfo;
    }

    @Transactional
    public boolean updateUser(String userProviderId, UpdateUserRequest updateUserRequest) {
        try {
            // 사용자 조회
            User user = userRepository.findByUserProviderId(userProviderId)
                    .orElseThrow(() -> new UserNotFoundException());

            // 사용자 정보 업데이트
            user.setUserProfileImg(updateUserRequest.getUserProfileImg()); // 프로필
            user.setUserNickname(updateUserRequest.getUserNickName()); // 닉네임
            user.setUserStaticBadge(updateUserRequest.getUserStaticBadge()); // 칭호
            userStyleService.updateUserHikingStyle(user.getUserId(),
                    updateUserRequest.getStyles()); // 등산 스타일

            // 사용자 정보 저장
            userRepository.save(user);

            return true;
        } catch (UserNotFoundException e) {
            // 사용자를 찾을 수 없는 경우
            throw e;
        } catch (Exception e) {
            // 일반적인 오류 발생 시
            throw new UserUpdateException("회원 정보 업데이트 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional
    public boolean deleteUser(String userProviderId) {
        try {

            userRepository.deleteByUserProviderId(userProviderId);

        } catch (Exception e) {

            throw new UserDeletionException("Failed to delete user with provider ID: " + userProviderId, e);

        }

        return true;
    }
}
