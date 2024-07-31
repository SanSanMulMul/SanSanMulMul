package com.sansantek.sansanmulmul.mountain.controller.summitstone;


import com.sansantek.sansanmulmul.mountain.domain.summitstone.Summitstone;
import com.sansantek.sansanmulmul.mountain.service.summitstone.SummitStoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/mountain")
@RequiredArgsConstructor
@Tag(name = "서미스톤 컨트롤러", description = "서미스톤 조회 기능 수행")
public class SummitStoneController {

    private final SummitStoneService summitstoneService;

    @GetMapping("/summitstone")
    @Operation(summary = "서미트스톤 전체 조회", description = "모든 서미트스톤을 조회합니다.")
    public ResponseEntity<Map<String, Object>> getAllSummitStone() {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            List<Summitstone> summitstones = summitstoneService.getAllSummitstones();
            resultMap.put("summitstones", summitstones);
            status = HttpStatus.OK; // 성공 시 200
        } catch (Exception e) {
            log.error("서미트스톤 전체 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.INTERNAL_SERVER_ERROR; // 실패 시 500
        }

        return new ResponseEntity<>(resultMap, status);
    }

}

