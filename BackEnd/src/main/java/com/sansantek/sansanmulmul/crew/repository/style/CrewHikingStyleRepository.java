package com.sansantek.sansanmulmul.crew.repository.style;

import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyle;
import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewHikingStyleRepository extends JpaRepository<CrewHikingStyle, CrewHikingStyleId> {
    List<CrewHikingStyle> findByStyle_HikingStylesId(int styleId);
}
