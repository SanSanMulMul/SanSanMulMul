package com.sansantek.sansanmulmul.user.repository.style;

import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.style.HikingStyle;
import com.sansantek.sansanmulmul.user.domain.style.UserHikingStyle;
import com.sansantek.sansanmulmul.user.domain.style.UserHikingStyleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserHikingStyleRepository extends JpaRepository<UserHikingStyle, UserHikingStyleId> {
    List<UserHikingStyle> findByUser_UserId(int userId);
    Optional<UserHikingStyle> findByUserAndStyle(User user, HikingStyle style);
    List<UserHikingStyle> findByUser(User user);
}
