package com.sansantek.sansanmulmul.user.service.login;

import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final UserRepository userRepository;

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
