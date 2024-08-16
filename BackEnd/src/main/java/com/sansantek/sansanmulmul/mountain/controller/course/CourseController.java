package com.sansantek.sansanmulmul.mountain.controller.course;

import com.sansantek.sansanmulmul.mountain.service.course.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mountain")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/{mountainId}/course")
    @Operation(summary = "산 코스 조회", description = "코스Id+코스갯수+코스이름")
    public ResponseEntity<Map<String, Object>> getCoursesByMountainId(@PathVariable int mountainId) {
        try {
            Map<String, Object> result = courseService.getCoursesByMountainId(mountainId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "내부 서버 오류가 발생했습니다."));
        }
    }
    @GetMapping("/{mountainId}/course/{courseId}")
    @Operation(summary = "산 코스 조회", description = "코스이름+코스난이도+코스길이+하행시간+상행시간+베스트구간+코스Id+트랙위도경도")
    public ResponseEntity<Map<String, Object>> getCourseDetail(@PathVariable int mountainId, @PathVariable Long courseId) {
        try {
            Map<String, Object> result = courseService.getCourseDetail(mountainId, courseId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "내부 서버 오류가 발생했습니다."));
        }
    }
}


