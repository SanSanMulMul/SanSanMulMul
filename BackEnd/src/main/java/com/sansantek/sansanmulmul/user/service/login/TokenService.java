package com.sansantek.sansanmulmul.user.service.login;

import com.sansantek.sansanmulmul.config.jwt.JwtToken;
import com.sansantek.sansanmulmul.config.jwt.JwtTokenProvider;
import com.sansantek.sansanmulmul.exception.auth.AuthenticationCreationException;
import com.sansantek.sansanmulmul.exception.auth.AuthenticationFailedException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final UserRepository userRepository;

    // JWT
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    // accessToken, refreshToken 발급
    @Transactional
    public JwtToken generateToken(String userProviderId, String rawPassword) {
        Authentication authentication;

        try {
            // 1. userProviderId를 기반으로 Authentication 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userProviderId, rawPassword);
            System.out.println("Authentication 객체 생성 성공");

            try {
                // 2. 실제 검증. authenticate() 메서드를 통해 요청된 User 에 대한 검증 진행
                authentication = authenticationManager.authenticate(authenticationToken);
                System.out.println("User 검증 성공");
            } catch (Exception e) {
                System.err.println("User 검증 실패: " + e.getMessage());
                throw new AuthenticationFailedException("User 검증에 실패했습니다.", e);
            }

        } catch (Exception e) {
            System.err.println("Authentication 객체 생성 실패: " + e.getMessage());
            throw new AuthenticationCreationException("Authentication 객체 생성에 실패했습니다.", e);
        }

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        // 4. 리프레시 토큰 저장
        saveRefreshToken(userProviderId, jwtToken.getRefreshToken());

        return jwtToken;
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
}
