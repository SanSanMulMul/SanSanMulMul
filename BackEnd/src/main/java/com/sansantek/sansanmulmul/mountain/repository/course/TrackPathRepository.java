package com.sansantek.sansanmulmul.mountain.repository.course;

import com.sansantek.sansanmulmul.mountain.domain.course.Track;
import com.sansantek.sansanmulmul.mountain.domain.course.TrackPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackPathRepository extends JpaRepository<TrackPath, Long> {
    List<TrackPath> findByTrack_TrackId(int trackId);
}
