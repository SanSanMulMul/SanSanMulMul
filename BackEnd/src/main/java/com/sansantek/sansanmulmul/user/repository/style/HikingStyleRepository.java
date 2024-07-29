package com.sansantek.sansanmulmul.user.repository.style;

import com.sansantek.sansanmulmul.user.domain.style.HikingStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HikingStyleRepository extends JpaRepository<HikingStyle, Integer> {
    Optional<HikingStyle> findByHikingStylesId(int hikingStylesId);
}
