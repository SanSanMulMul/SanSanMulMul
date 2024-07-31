package com.sansantek.sansanmulmul.user.repository.badge;

import com.sansantek.sansanmulmul.user.domain.badge.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Integer> {
    Optional<Badge> findByBadgeId(int badgeId);
}
