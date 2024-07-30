package com.sansantek.sansanmulmul.user.service;

import com.sansantek.sansanmulmul.config.jwt.JwtToken;
import com.sansantek.sansanmulmul.config.jwt.JwtTokenProvider;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.dto.request.SignUpUserRequest;
import com.sansantek.sansanmulmul.user.dto.request.UpdateUserRequest;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import com.sansantek.sansanmulmul.user.service.login.TokenService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Transactional
    public User signUpUser(SignUpUserRequest signUpUserRequest) {
//        String encoder = passwordEncoder.encode(signUpUserRequest.getUserPassword());
        String encoder = passwordEncoder.encode("");

        User user = new User(
                signUpUserRequest.getUserProviderId(),
                encoder,
                signUpUserRequest.getUserName(),
                signUpUserRequest.getUserNickName(),
                signUpUserRequest.getUserGender(),
                signUpUserRequest.getUserProfileImg(),
                signUpUserRequest.getUserBirth(),
                signUpUserRequest.isUserIsAdmin()
        );

        userRepository.save(user);
        userRepository.flush();
        return user;
    }

    @Transactional
    public JwtToken signIn(String userProviderId, String rawPassword) {
        // 1. userProviderId를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userProviderId, rawPassword);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 User 에 대한 검증 진행
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        // 4. 리프레시 토큰 저장
        tokenService.saveRefreshToken(userProviderId, jwtToken.getRefreshToken());

        return jwtToken;
    }

    public boolean isExistsUser(String userProviderId) {
        return userRepository.existsByUserProviderId(userProviderId);
    }

    public User getUser(String userProviderId) {
        return userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getUser(int userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public boolean isExistsUserNickname(String userNickname) {
        return userRepository.existsByUserNickname(userNickname);
    }

    public User updateUser(String userProviderId, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

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

        user.setUserTotalLength(updateUserRequest.getUserTotalLength());
        user.setUserTotalElevation(updateUserRequest.getUserTotalElevation());
        user.setUserTotalSteps(updateUserRequest.getUserTotalSteps());
        user.setUserTotalKcal(updateUserRequest.getUserTotalKcal());
        user.setUserTotalHiking(updateUserRequest.getUserTotalHiking());
        user.setUserStoneCount(updateUserRequest.getUserStoneCount());

        return userRepository.save(user);
    }
}
