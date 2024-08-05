package com.sansantek.sansanmulmul.crew.repository;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyle;
import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyleId;
import com.sansantek.sansanmulmul.user.domain.style.HikingStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewHikingStyleRepository extends JpaRepository<CrewHikingStyle, CrewHikingStyleId> {
    List<CrewHikingStyle> findByStyle_HikingStylesId(int styleId);
    Optional<CrewHikingStyle> findByCrewAndStyle(Crew crew, HikingStyle style);
}
