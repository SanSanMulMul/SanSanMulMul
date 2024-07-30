package com.sansantek.sansanmulmul.user.service;

import com.sansantek.sansanmulmul.config.jwt.JwtToken;
import com.sansantek.sansanmulmul.config.jwt.JwtTokenProvider;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.dto.request.SignUpUserRequest;
import com.sansantek.sansanmulmul.user.dto.request.UpdateUserRequest;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import com.sansantek.sansanmulmul.user.service.login.TokenService;
import com.sansantek.sansanmulmul.user.service.style.StyleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    // Spring Security
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // JWT
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    // service
    private final StyleService styleService;

    // Repository
    private final UserRepository userRepository;

    @Transactional
    public User signUpUser(SignUpUserRequest signUpUserRequest, String password) {
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

    @Transactional
    public User updateUser(String userProviderId, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (updateUserRequest.getUserNickName() != null)
            user.setUserNickname(updateUserRequest.getUserNickName());

        if (updateUserRequest.getUserGender() != null)
            user.setUserGender(updateUserRequest.getUserGender());

        if (updateUserRequest.getUserProfileImg() != null)
            user.setUserProfileImg(updateUserRequest.getUserProfileImg());

        if (updateUserRequest.getUserBirth() != null)
            user.setUserBirth(updateUserRequest.getUserBirth());


        return userRepository.save(user);
    }
}
