package com.sansantek.sansanmulmul.user.controller.summitstone;

import com.sansantek.sansanmulmul.config.jwt.JwtTokenProvider;
import com.sansantek.sansanmulmul.exception.InvalidTokenException;
import com.sansantek.sansanmulmul.user.service.summitstone.SummitstoneService;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.service.UserService;
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
@RequestMapping("/user/stone")
@Tag(name = "회원 정상석 컨트롤러", description = "회원 정상석의 관한 모든 기능 수행")
public class SummitstoneController {

    // service
    private final UserService userService;
    private final SummitstoneService summitstoneService;

    // JWT
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    @Operation(summary = "회원 인증 정상석 조회", description = "회원이 인증한 정상석 조회")
    public ResponseEntity<Map<String, Object>> getUserStone
            (@RequestHeader("Authorization") String accessToken) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        log.debug("user accessToken: {}", accessToken);

        try {
            // Authorization 헤더에서 "Bearer " 접두사를 제거
            String token = accessToken.substring(7);

            // 액세스 토큰 유효성 검증
            if (jwtTokenProvider.validateToken(token)) {
                String userProviderId = jwtTokenProvider.getUserProviderId(token);

                // 해당 사용자 가져오기
                User user = userService.getUser(userProviderId);

                // 사용자 인증 정상석 조회
                List<String[]> userStoneList = summitstoneService.getStoneList(user.getUserId());

                // JSON으로 결과 전송
                resultMap.put("userStoneList Size: ", userStoneList.size());
                for (int i = 0; i < userStoneList.size(); i++) {
                    resultMap.put("stone_id: " + i, userStoneList.get(i)[0]);
                    resultMap.put("stone_name: " + i, userStoneList.get(i)[1]);
                }

                status = HttpStatus.OK; // 200

            } else {
                throw new Exception("Invalid Token");
            }

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
            (@RequestHeader("Authorization") String accessToken,
             @RequestParam int summitstoneId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        log.debug("user accessToken: {}", accessToken);

        try {
            // Authorization 헤더에서 "Bearer " 접두사를 제거
            String token = accessToken.substring(7);

            // 액세스 토큰 유효성 검증
            if (jwtTokenProvider.validateToken(token)) {
                String userProviderId = jwtTokenProvider.getUserProviderId(token);

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 인증 정상석 조회
            summitstoneService.addStone(user.getUserId(), summitstoneId);

            // JSON으로 결과 전송
            resultMap.put("userId: ", user.getUserId());
            resultMap.put("userProviderId: ", user.getUserProviderId());
            resultMap.put("add summitstone Id: ", summitstoneId);

            status = HttpStatus.OK; // 200

            } else {
                throw new Exception("Invalid Token");
            }

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
}
