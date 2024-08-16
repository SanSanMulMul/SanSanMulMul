package com.sansantek.sansanmulmul.user.repository.badge;

import com.sansantek.sansanmulmul.user.domain.badge.ChkBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChkBadgeRepository extends JpaRepository<ChkBadge, Integer> {
    Optional<ChkBadge> findByUser_UserId(int userId);
}
