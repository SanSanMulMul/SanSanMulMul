package com.sansantek.sansanmulmul.user.repository;

import com.sansantek.sansanmulmul.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // 조회
    Optional<User> findByUserId(int userId);
    Optional<User> findByUserProviderId(String userProviderId);
    Optional<String> findUserRefreshTokenByUserProviderId(String userProviderId);
    int findUserIdByUserProviderId(String userProviderId);

    // 유무판단
    boolean existsByUserProviderId(String userProviderId);
    boolean existsByUserNickname(String userNickname);

    // 저장
    User save(User user);

    // 삭제
    void deleteByUserProviderId(String userProviderId);

}
