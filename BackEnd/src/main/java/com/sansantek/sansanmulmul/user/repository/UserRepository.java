package com.sansantek.sansanmulmul.user.repository;

import com.sansantek.sansanmulmul.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserId(Integer userId);
    Optional<User> findByUserProviderId(String userProviderId);
    Optional<User> findByUserRefreshToken(String userRefreshToken);
    Optional<String> findUserRefreshTokenByUserProviderId(String userProviderId);
    String findUserProviderIdByUserId(int userId);

    boolean existsByUserProviderId(String userProviderId);
    boolean existsByUserNickname(String userNickname);

    User save(User user);
}
