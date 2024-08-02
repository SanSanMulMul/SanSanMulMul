package com.sansantek.sansanmulmul.mountain.repository.course;

import com.sansantek.sansanmulmul.mountain.domain.course.CourseTracks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseTracksRepository extends JpaRepository<CourseTracks, Integer> {
    List<CourseTracks> findByCourse_CourseId(Long courseId);
}