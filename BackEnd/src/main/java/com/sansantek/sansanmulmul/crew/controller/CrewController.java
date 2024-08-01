package com.sansantek.sansanmulmul.crew.controller;

import com.sansantek.sansanmulmul.crew.dto.response.CrewResponse;
import com.sansantek.sansanmulmul.crew.service.CrewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/crew")
@Tag(name = "그룹 정보 컨트롤러", description = "그룹 정보관련 기능 수행")
public class CrewController {

    // service
    private final CrewService crewService;

    @GetMapping("/all")
    @Operation(summary = "그룹 정보 전체 조회", description = "그룹에 대한 정보를 전체 조회")
    public ResponseEntity<?> getAllCrews() {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            List<CrewResponse> crewResponse = crewService.getAllCrews();

            return new ResponseEntity<>(crewResponse, status);

        } catch (Exception e) {
            status = HttpStatus.BAD_REQUEST;

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }
}
