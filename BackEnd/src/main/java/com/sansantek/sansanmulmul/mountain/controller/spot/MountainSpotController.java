package com.sansantek.sansanmulmul.mountain.controller.spot;

import com.sansantek.sansanmulmul.mountain.domain.spot.MountainSpot;
import com.sansantek.sansanmulmul.mountain.service.spot.MountainSpotService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mountain")
@Tag(name = "산 스팟 컨트롤러", description = "산 스팟 정보 조회 기능 수행")
public class MountainSpotController {

    private final MountainSpotService mountainSpotService;

    @GetMapping("/spots")
    @Operation(summary = "모든 스팟 조회", description = "모든 산 스팟 정보를 조회")
    public ResponseEntity<Map<String, Object>> getAllSpots() {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            List<MountainSpot> spots = mountainSpotService.getAllSpots();
            resultMap.put("spots", spots);
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("스팟 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/spots/detail/{detail}")
    @Operation(summary = "디테일에 따른 스팟 조회", description = "디테일(화장실, 음수대, 운동, 주차장)에 따른 산 스팟 정보를 조회")
    public ResponseEntity<Map<String, Object>> getSpotsByDetail(@PathVariable String detail) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            List<MountainSpot> spots = mountainSpotService.getSpotsByDetail(detail);
            resultMap.put("spots", spots);
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("디테일에 따른 스팟 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/spots/{mountainCode}")
    @Operation(summary = "산 코드에 따른 스팟 조회", description = "산 코드에 따른 산 스팟 정보를 조회")
    public ResponseEntity<Map<String, Object>> getSpotsByMountainCode(@PathVariable int mountainCode) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            List<MountainSpot> spots = mountainSpotService.getSpotsByMountainCode(mountainCode);
            resultMap.put("spots", spots);
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("산 코드에 따른 스팟 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}