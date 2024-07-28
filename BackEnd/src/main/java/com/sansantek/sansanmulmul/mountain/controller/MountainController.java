package com.sansantek.sansanmulmul.mountain.controller;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.service.MountainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mountain")
@Tag(name = "산 컨트롤러", description = "산 관련 모든 기능 수행")
public class MountainController {

    private final MountainService mountainService;

    @GetMapping
    @Operation(summary = "산 전체 조회", description = "산Id+산Code+산 이름+산 위치+산 높이+산 상세설명+산 이미지+산 계절")
    public ResponseEntity<Map<String, Object>> getAllMountains() {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            List<Mountain> mountains = mountainService.getAllMountains();
            resultMap.put("mountains", mountains);
            status = HttpStatus.OK; // 성공 시 200
        } catch (Exception e) {
            log.error("산 전체 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.INTERNAL_SERVER_ERROR; // 실패 시 500
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/{mountain_id}")
    @Operation(summary = "산 상세 조회", description = "산Id+산Code+산 이름+산 위치+산 높이+산 상세설명+산 이미지+산 계절")
    public ResponseEntity<Map<String, Object>> getMountainDetail(@PathVariable Long mountain_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            Mountain mountain = mountainService.getMountainDetail(mountain_id);
            resultMap.put("mountain", mountain);
            status = HttpStatus.OK; // 성공 시 200
        } catch (NoSuchElementException e) {
            log.error("산 상세 조회 실패: {}", e.getMessage());
            resultMap.put("error", "Mountain not found");
            status = HttpStatus.NOT_FOUND; // 요청한 산을 찾을 수 없을 때 404
        } catch (Exception e) {
            log.error("산 상세 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.INTERNAL_SERVER_ERROR; // 그 외 실패 시 500
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
