package com.sansantek.sansanmulmul.group.repository.style;

import com.sansantek.sansanmulmul.group.domain.style.GroupHikingStyle;
import com.sansantek.sansanmulmul.group.domain.style.GroupHikingStyleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupHikingStyleRepository extends JpaRepository<GroupHikingStyle, GroupHikingStyleId> {
    List<GroupHikingStyle> findByStyle_HikingStylesId(int styleId);
}
