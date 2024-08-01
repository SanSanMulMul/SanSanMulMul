package com.sansantek.sansanmulmul.crew.controller;

import com.sansantek.sansanmulmul.crew.dto.response.CrewStyleResponse;
import com.sansantek.sansanmulmul.crew.service.style.CrewStyleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/crew/style")
@Tag(name = "그룹 등산 스타일 정보 컨트롤러", description = "그룹 등산 스타일 정보관련 기능 수행")
public class CrewStyleController {

    // service
    private final CrewStyleService crewStyleService;

    @GetMapping
    @Operation(summary = "특정 등산 스타일 조회", description = "해당 등산 스타일에 해당하는 그룹 조회")
    public ResponseEntity<?> getHikingStyles(@RequestParam("styleId") int styleId) {
//        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {

            // 특정 스타일에 해당하는 그룹 정보 조회
            List<CrewStyleResponse> crewStyleResponseList = crewStyleService.getCrewList(styleId);

            // JSON으로 결과 전송
//            resultMap.put("crewStyleResponseList", crewStyleResponseList);

            return new ResponseEntity<>(crewStyleResponseList, status);

        } catch ( Exception e ) {
            status = HttpStatus.BAD_REQUEST; // 400
            
        return new ResponseEntity<>(e.getMessage(), status);
        }
    }

}
