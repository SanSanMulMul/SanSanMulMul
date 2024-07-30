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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    // 회원가입 진행
    @Transactional
    public User signUpUser(SignUpUserRequest signUpUserRequest) {
        String encoder = passwordEncoder.encode(signUpUserRequest.getUserPassword());

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
    public JwtToken signIn(String userProviderId, String password) {
        // 1. userProviderId를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userProviderId, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 User 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        System.out.println("2 여기가 안도 ㅐㅅㅂ");

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        // 4. 리프레시 토큰 저장
        tokenService.saveRefreshToken(userProviderId, jwtToken.getAccessToken());

        return jwtToken;
    }

    // DB에 저장되어 있는 회원 확인
    public boolean isExistsUser(String userProviderId) {
        // true: 회원이 저장되어 있음
        // false: 회원이 저장되어 있지 않음
        return userRepository.existsByUserProviderId(userProviderId);
    }

    // DB에 저장되어 있는 회원 찾기(userProviderId을 사용)
    public User getUser(String userProviderId) {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user;
    }

    // DB에 저장되어 있는 회원 찾기(userId을 사용)
    public User getUser(int userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user;
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

    @Override
    public UserDetails loadUserByUsername(String userProviderId) throws UsernameNotFoundException {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 회원을 찾을 수 없습니다."));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserProviderId())
                .password(user.getUserPassword()) // 데이터베이스에서 가져온 인코딩된 비밀번호
                .roles(user.isUserIsAdmin() ? "ADMIN" : "USER")
                .build();
    }
}
