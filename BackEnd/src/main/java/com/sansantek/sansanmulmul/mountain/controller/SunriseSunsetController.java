package com.sansantek.sansanmulmul.mountain.controller;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.service.MountainService;
import com.sansantek.sansanmulmul.util.SunriseSunsetService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/mountain")
public class SunriseSunsetController {

    @Autowired
    private MountainService mountainService;

    @Autowired
    private SunriseSunsetService sunriseSunsetService;

    @GetMapping("/sun/{mountain_id}")
    @Operation(summary = "산 일출일몰 조회", description = "산의 위도와 경도를 사용해 일출 및 일몰 시간을 조회")
    public ResponseEntity<List<Map<String, String>>> getSunriseSunsetTimes(@PathVariable("mountain_id") int mountainId) {
        List<Map<String, String>> resultList = new ArrayList<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            Mountain mountain = mountainService.getMountainDetail(mountainId);
            double latitude = mountain.getMountainLat();
            double longitude = mountain.getMountainLon();

            Map<String, String> sunriseSunsetTimes = sunriseSunsetService.getSunriseSunsetTimes(latitude, longitude);
            resultList.add(sunriseSunsetTimes);

            status = HttpStatus.OK; // 성공 시 200
        } catch (NoSuchElementException e) {
            log.error("산 상세 조회 실패: {}", e.getMessage());
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "Mountain not found");
            resultList.add(errorMap);
            status = HttpStatus.NOT_FOUND; // 요청한 산을 찾을 수 없을 때 404
        } catch (Exception e) {
            log.error("일출일몰 조회 실패: {}", e.getMessage());
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "An unexpected error occurred");
            resultList.add(errorMap);
            status = HttpStatus.INTERNAL_SERVER_ERROR; // 그 외 실패 시 500
        }

        return new ResponseEntity<>(resultList, status);
    }
}