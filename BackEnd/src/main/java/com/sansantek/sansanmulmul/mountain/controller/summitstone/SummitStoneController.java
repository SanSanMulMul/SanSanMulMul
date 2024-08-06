package com.sansantek.sansanmulmul.mountain.controller.summitstone;


import com.sansantek.sansanmulmul.mountain.domain.summitstone.Summitstone;
import com.sansantek.sansanmulmul.mountain.service.summitstone.SummitStoneService;
import com.sansantek.sansanmulmul.user.dto.response.StoneResponse;
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
@RequestMapping("/mountain/summitstone")
@RequiredArgsConstructor
@Tag(name = "정상석 컨트롤러", description = "정상석에 관한 모든 기능 수행")
public class SummitStoneController {

    private final SummitStoneService summitstoneService;

    @GetMapping("/all")
    @Operation(summary = "정상석 전체 조회", description = "모든 정상석을 조회")
    public ResponseEntity<?> getAllSummitStone() {

        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            List<StoneResponse> summitstones = summitstoneService.getStoneList();

            status = HttpStatus.OK; // 200
            return new ResponseEntity<>(summitstones, status);
            
        } catch (Exception e) {

            log.error("서미트스톤 전체 조회 실패: {}", e.getMessage());
            status = HttpStatus.NOT_FOUND; // 404
            return new ResponseEntity<>(e.getMessage(), status);

        }
    }

    @GetMapping("/detail")
    @Operation(summary = "정상석 상세 조회", description = "해당 정상석을 상세 조회")
    public ResponseEntity<?> getDetailSummitStone
            (@RequestParam int summitStoneId) {
        HttpStatus status = HttpStatus.ACCEPTED;

        return new ResponseEntity<>(status);
    }

}

