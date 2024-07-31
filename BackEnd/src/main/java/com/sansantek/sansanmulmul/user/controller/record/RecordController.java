package com.sansantek.sansanmulmul.user.controller.record;

import com.sansantek.sansanmulmul.exception.auth.InvalidTokenException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.service.UserService;
import com.sansantek.sansanmulmul.user.service.record.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/record")
@Tag(name = "회원 등산 기록 컨트롤러", description = "회원 등산 기록의 관한 모든 기능 수행")
public class RecordController {

    // service
    private final UserService userService;
    private final RecordService recordService;

    @GetMapping
    @Operation(summary = "회원 전체 등산 기록 조회", description = "액세스 토큰을 사용해 회원 전체 등산 기록 조회")
    public ResponseEntity<Map<String, Object>> getUserRecord
            (Authentication authentication) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 해당 사용자 등산 기록 가져오기


            // JSON으로 결과 전송

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED; // 401

        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping
    @Operation(summary = "회원 전체 등산 기록 조회", description = "액세스 토큰을 사용해 회원 전체 등산 기록 조회")
    public ResponseEntity<Map<String, Object>> getUserRecordDetails
            (Authentication authentication,
             @RequestParam int recordId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 해당 사용자 등산 기록 가져오기


            // JSON으로 결과 전송

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED; // 401

        }

        return new ResponseEntity<>(resultMap, status);
    }

}
