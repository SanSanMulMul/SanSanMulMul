package com.sansantek.sansanmulmul.user.repository.badge;

import com.sansantek.sansanmulmul.user.domain.badge.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Integer> {
    List<UserBadge> findByUser_UserId(int userId);
}