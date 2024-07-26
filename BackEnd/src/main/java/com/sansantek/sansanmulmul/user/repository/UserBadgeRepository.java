package com.sansantek.sansanmulmul.user.repository;

import com.sansantek.sansanmulmul.user.domain.badge.UserBadge;
import com.sansantek.sansanmulmul.user.domain.badge.UserBadgeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, UserBadgeId> {
    List<UserBadge> findByUser_UserId(int userId);
}