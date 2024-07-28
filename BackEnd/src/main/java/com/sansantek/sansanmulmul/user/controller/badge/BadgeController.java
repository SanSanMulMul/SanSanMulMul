package com.sansantek.sansanmulmul.user.controller.badge;

import com.sansantek.sansanmulmul.exception.InvalidTokenException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.badge.Badge;
import com.sansantek.sansanmulmul.user.service.badge.BadgeService;
import com.sansantek.sansanmulmul.user.service.UserService;
import com.sansantek.sansanmulmul.config.jwt.JwtTokenProvider;
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
@RequestMapping("/user/badge")
@Tag(name = "회원 칭호 컨트롤러", description = "회원 칭호의 관한 모든 기능 수행")
public class BadgeController {

    // service
    private final UserService userService;
    private final BadgeService badgeService;

    // JWT
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    @Operation(summary = "회원 전체 칭호 조회", description = "액세스 토큰을 사용해 회원 칭호 조회")
    public ResponseEntity<Map<String, Object>> getUserBadge
    (@RequestHeader("Authorization") String accessToken) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        log.debug("user accessToken: {}", accessToken);

        try {
            // Authorization 헤더에서 "Bearer " 접두사를 제거
            String token = accessToken.substring(7);

            // 액세스 토큰 유효성 검증
            if (jwtTokenProvider.checkToken(token)) {
                String userProviderId = jwtTokenProvider.getUserProviderId(token);

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 칭호 조회
            List<String> userBadgeList = badgeService.getBadgeList(user.getUserId());
            
            // JSON으로 결과 전송
            resultMap.put("userBadgeList Size", userBadgeList.size());
            for (int i = 0; i < userBadgeList.size(); i++) 
                resultMap.put("userBadge " + i, userBadgeList.get(i));
            
            status = HttpStatus.OK; // 200
            } else {
                throw new Exception("Invalid Token");
            }
        } catch (InvalidTokenException e) {
            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED; // 401
        } catch (Exception e) {
            log.error("회원 칭호 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping
    @Operation(summary = "회원 칭호 추가", description = "액세스 토큰을 사용해 회원 칭호 추가")
    public ResponseEntity<Map<String, Object>> addBadge
            (@RequestHeader("Authorization") String accessToken,
             @RequestParam("badgeId") int badgeId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        log.debug("user accessToken: {}", accessToken);

        try {
            // Authorization 헤더에서 "Bearer " 접두사를 제거
            String token = accessToken.substring(7);

            // 액세스 토큰 유효성 검증
            if (jwtTokenProvider.checkToken(token)) {
                String userProviderId = jwtTokenProvider.getUserProviderId(token);

                // 해당 사용자 가져오기
                User user = userService.getUser(userProviderId);

                // 사용자 인증 칭호 추가
                badgeService.addBadge(user.getUserId(), badgeId);

                // JSON으로 결과 전송
                resultMap.put("userId: ", user.getUserId());
                resultMap.put("userProviderId: ", user.getUserProviderId());
                resultMap.put("add Badge Id: ", badgeId);

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

    @PatchMapping
    @Operation(summary = "회원 칭호 수정", description = "액세스 토큰을 사용해 회원 칭호 수정")
    public ResponseEntity<Map<String, Object>> updateUserBadge(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam("badgeId") int badgeId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

         log.debug("user accessToken: {}", accessToken);

        try {
            // Authorization 헤더에서 "Bearer " 접두사를 제거
            String token = accessToken.substring(7);

            // 액세스 토큰 유효성 검증
            if (jwtTokenProvider.checkToken(token)) {
                String userProviderId = jwtTokenProvider.getUserProviderId(token);

                // 해당 사용자 가져오기
                User user = userService.getUser(userProviderId);

                // 기존의 칭호 변경
                badgeService.updateBadList(user.getUserId(), badgeId);

                status = HttpStatus.OK; // 200

            } else {
                throw new Exception("Invalid Token");
            }

        } catch (Exception e) {
            log.error("회원 칭호 수정 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.BAD_REQUEST; // 400
        }

        return new ResponseEntity<>(resultMap, status);
    }

}
