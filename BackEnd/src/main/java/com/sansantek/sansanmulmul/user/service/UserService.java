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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    // Spring Security
    private final PasswordEncoder passwordEncoder;

    // service
    private final StyleService styleService;

    // Repository
    private final UserRepository userRepository;

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
