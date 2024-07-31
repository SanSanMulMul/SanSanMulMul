package com.sansantek.sansanmulmul.mountain.controller;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.domain.spot.MountainSpot;
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
    public ResponseEntity<Map<String, Object>> getMountainDetail(@PathVariable int mountain_id) {
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
    @GetMapping("/{mountain_id}/spots/toilet")
    @Operation(summary = "산의 화장실 위치 조회", description = "특정 산의 화장실 위치를 조회합니다")
    public ResponseEntity<Map<String, Object>> getMountainSpotsWithToilet(@PathVariable int mountain_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            List<MountainSpot> toiletSpots = mountainService.getMountainSpotsWithDetail(mountain_id, "화장실");
            resultMap.put("toiletSpots", toiletSpots);
            status = HttpStatus.OK; // 성공 시 200
        } catch (NoSuchElementException e) {
            log.error("화장실 위치 조회 실패: {}", e.getMessage());
            resultMap.put("error", "Mountain or spots not found");
            status = HttpStatus.NOT_FOUND; // 요청한 산 또는 화장실을 찾을 수 없을 때 404
        } catch (Exception e) {
            log.error("화장실 위치 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.INTERNAL_SERVER_ERROR; // 그 외 실패 시 500
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/{mountain_id}/spots/parking")
    @Operation(summary = "산의 주차장 위치 조회", description = "특정 산의 주차장 위치를 조회합니다")
    public ResponseEntity<Map<String, Object>> getMountainSpotsWithParking(@PathVariable int mountain_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            List<MountainSpot> parkingSpots = mountainService.getMountainSpotsWithDetail(mountain_id, "주차장");
            resultMap.put("parkingSpots", parkingSpots);
            status = HttpStatus.OK; // 성공 시 200
        } catch (NoSuchElementException e) {
            log.error("주차장 위치 조회 실패: {}", e.getMessage());
            resultMap.put("error", "Mountain or spots not found");
            status = HttpStatus.NOT_FOUND; // 요청한 산 또는 주차장을 찾을 수 없을 때 404
        } catch (Exception e) {
            log.error("주차장 위치 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.INTERNAL_SERVER_ERROR; // 그 외 실패 시 500
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/{mountain_id}/spots/water")
    @Operation(summary = "산의 음수대 및 약수터 위치 조회", description = "특정 산의 음수대 및 약수터 위치를 조회합니다")
    public ResponseEntity<Map<String, Object>> getMountainSpotsWithWater(@PathVariable int mountain_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            List<MountainSpot> waterSpots = mountainService.getMountainSpotsWithDetails(mountain_id, "음수대", "약수터","샘터");
            resultMap.put("waterSpots", waterSpots);
            status = HttpStatus.OK; // 성공 시 200
        } catch (NoSuchElementException e) {
            log.error("음수대 및 약수터 위치 조회 실패: {}", e.getMessage());
            resultMap.put("error", "Mountain or spots not found");
            status = HttpStatus.NOT_FOUND; // 요청한 산 또는 음수대/약수터를 찾을 수 없을 때 404
        } catch (Exception e) {
            log.error("음수대 및 약수터 위치 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.INTERNAL_SERVER_ERROR; // 그 외 실패 시 500
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/{mountain_id}/spots/dangerous")
    @Operation(summary = "산의 위험지역 위치 조회", description = "특정 산의 위험지역 위치를 조회합니다")
    public ResponseEntity<Map<String, Object>> getMountainSpotsWithDangerous(@PathVariable int mountain_id) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            List<MountainSpot> waterSpots = mountainService.getMountainSpotsWithDetails(mountain_id, "위험지역","위험지역,이정표","폐쇄","근계노출","암벽,급경사,낙석","암석지대","위험안내판","암벽, 급경사, 낙석","위험지역(밧줄)","위험(암석길)","매우위험(절벽)","위험지역(침식위험)","위험(밧줄)","위험안내","사고지역","위험구간시작","위험구간끝","위험구간시작점","위험구간중간","위험구간끝점","위험지역시작","위험지역중간","위험지역끝","위험지대","위험지","계곡 위험","급경사1","급경사2","급경사3","위험지","위험지,안내판","위험지,조망대");
            resultMap.put("waterSpots", waterSpots);
            status = HttpStatus.OK; // 성공 시 200
        } catch (NoSuchElementException e) {
            log.error("위험지역 조회 실패: {}", e.getMessage());
            resultMap.put("error", "Mountain or spots not found");
            status = HttpStatus.NOT_FOUND; // 요청한 산 또는 음수대/약수터를 찾을 수 없을 때 404
        } catch (Exception e) {
            log.error("위험지역 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.INTERNAL_SERVER_ERROR; // 그 외 실패 시 500
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
