package com.sansantek.sansanmulmul.mountain.service.course;


import com.sansantek.sansanmulmul.mountain.domain.course.Course;
import com.sansantek.sansanmulmul.mountain.domain.course.CourseTracks;
import com.sansantek.sansanmulmul.mountain.repository.MountainRepository;
import com.sansantek.sansanmulmul.mountain.repository.course.CourseRepository;
import com.sansantek.sansanmulmul.mountain.repository.course.CourseTracksRepository;
import com.sansantek.sansanmulmul.mountain.repository.course.TrackPathRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    @Autowired
    private final MountainRepository mountainRepository;
    @Autowired
    private final CourseRepository courseRepository;
    @Autowired
    private final CourseTracksRepository courseTracksRepository;
    @Autowired
    private final TrackPathRepository trackPathRepository;

//    public List<CourseResponse> getCourseDetail(Long course_id) {
//        List<CourseResponse> crewCourse = new ArrayList<>();
//
//        List<Course> CourseList = courseRepository.findByCourseId(course_id);
//        for (Course course : CourseList) {
//            CourseResponse cr = new CourseResponse(
//                    course.getCourseId(),
//                    course.getCourseName(),
//                    course.getCourseLength(),
//                    course.getCourseUptime(),
//                    course.getCourseDowntime(),
//                    course.getCourseLevel()
//            );
//            crewCourse.add(cr);
//        }
//        return crewCourse;
//    }

    public Map<String, Object> getCoursesByMountainId(int mountainId) {
        int mountainCode = mountainRepository.findById(mountainId)
                .orElseThrow(() -> new RuntimeException("산을 찾을 수 없습니다."))
                .getMountainCode();
        List<Course> courses = courseRepository.findByMountain_MountainCode(mountainCode);

        Map<String, Object> result = new HashMap<>();
        result.put("courseCount", courses.size());
        result.put("courseNames", courses.stream().map(Course::getCourseName).collect(Collectors.toList()));
        result.put("courseIds", courses.stream().map(Course::getCourseId).collect(Collectors.toList()));
        return result;
    }


    public Map<String, Object> getCourseDetail(int mountainId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("코스를 찾을 수 없습니다."));

        if (course.getMountain().getMountainId() != mountainId) {
            throw new RuntimeException("해당 코스는 이 산에 속하지 않습니다.");
        }

        List<CourseTracks> courseTracks = courseTracksRepository.findByCourse_CourseId(courseId);

        Map<String, Object> result = new HashMap<>();
        result.put("courseId", course.getCourseId());
        result.put("courseName", course.getCourseName());
        result.put("courseLength", course.getCourseLength());
        result.put("courseUptime", course.getCourseUptime());
        result.put("courseDowntime", course.getCourseDowntime());
        result.put("courseLevel", course.getCourseLevel());
        result.put("courseBestTrackId", course.getCourseBestTrackId());
        result.put("tracks", courseTracks.stream().map(courseTrack -> {
            Map<String, Object> trackInfo = new HashMap<>();
//            trackInfo.put("trackId", courseTrack.getTrack().getTrackId());
//            trackInfo.put("trackName", courseTrack.getTrack().getTrackName());
//            trackInfo.put("trackLength", courseTrack.getTrack().getTrackLength());
//            trackInfo.put("trackLevel", courseTrack.getTrack().getTrackLevel());
//            trackInfo.put("trackUptime", courseTrack.getTrack().getTrackUptime());
//            trackInfo.put("trackDowntime", courseTrack.getTrack().getTrackDowntime());
//            trackInfo.put("trackBestPath", courseTrack.getTrack().getTrackBestPath());
            trackInfo.put("trackPaths", trackPathRepository.findByTrack_TrackId(courseTrack.getTrack().getTrackId()).stream().map(trackPath -> {
                Map<String, Object> trackPathInfo = new HashMap<>();
                trackPathInfo.put("trackPathId", trackPath.getTrackPathId());
                trackPathInfo.put("trackPathLat", trackPath.getTrackPathLat());
                trackPathInfo.put("trackPathLon", trackPath.getTrackPathLon());
                return trackPathInfo;
            }).collect(Collectors.toList()));
            return trackInfo;
        }).collect(Collectors.toList()));

        return result;
    }
}