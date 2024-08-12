package com.sansantek.sansanmulmul.crew.repository;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.crewalarm.CrewAlarm;
import com.sansantek.sansanmulmul.crew.domain.crewgallery.CrewGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewAlarmRepository extends JpaRepository<CrewAlarm, Integer> {
    List<CrewAlarm> findByCrew(Crew crew);
}

