package com.sansantek.sansanmulmul.user.controller.summitstone;

import com.sansantek.sansanmulmul.exception.auth.InvalidTokenException;
import com.sansantek.sansanmulmul.user.dto.response.StoneResponse;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.service.UserService;
import com.sansantek.sansanmulmul.mountain.service.summitstone.SummitStoneService;
import com.sansantek.sansanmulmul.user.service.summitstone.SummitstoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/stone")
@Tag(name = "회원 정상석 컨트롤러", description = "회원 정상석의 관한 모든 기능 수행")
public class SummitstoneController {

    // service
    private final UserService userService;
    private final SummitStoneService mountainSummitstoneService; // mountain Service
    private final SummitstoneService userSummitstoneService; // user Service

    @GetMapping
    @Operation(summary = "회원 인증 정상석 조회", description = "회원이 인증한 정상석 조회")
    public ResponseEntity<Map<String, Object>> getUserStone
            (Authentication authentication) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 인증 정상석 조회
            List<StoneResponse> userStoneList = userSummitstoneService.getStoneListByUser(user.getUserId());

            // JSON으로 결과 전송
            resultMap.put("userStoneListSize", userStoneList.size());
            resultMap.put("userStoneList", userStoneList);

            status = HttpStatus.OK; // 200


        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED; // 401

        } catch (Exception e) {

            log.error("회원 정상석 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.BAD_REQUEST; // 400

        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping
    @Operation(summary = "회원 인증 정상석 추가", description = "회원이 인증한 정상석 조회")
    public ResponseEntity<Map<String, Object>> addUserStone
            (Authentication authentication,
             @RequestParam int summitstoneId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 인증 정상석 조회
            userSummitstoneService.addStone(user.getUserId(), summitstoneId);

            // JSON으로 결과 전송
            resultMap.put("userId", user.getUserId());
            resultMap.put("userProviderId", user.getUserProviderId());
            resultMap.put("addSummitstoneId", summitstoneId);

            status = HttpStatus.OK; // 200

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED; // 401

        } catch (Exception e) {

            log.error("회원 정상석 추가 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.BAD_REQUEST; // 400

        }

        return new ResponseEntity<>(resultMap, status);
    }
    
    @GetMapping("/all")
    @Operation(summary = "전체 정상석 조회", description = "100대 명산의 전체 정상석 조회")
    public ResponseEntity<?> getAllSummitstones() {
        HttpStatus status = HttpStatus.ACCEPTED;
        
        try {
            
            // 전체 정상석 리스트 조회
            List<StoneResponse> stones = mountainSummitstoneService.getStoneList();

            status = HttpStatus.OK; // 200
            return new ResponseEntity<>(stones, status);

        } catch (Exception e) {

            status = HttpStatus.NOT_FOUND; // 404
            return new ResponseEntity<>(e.getMessage(), status);

        }
    }
}
