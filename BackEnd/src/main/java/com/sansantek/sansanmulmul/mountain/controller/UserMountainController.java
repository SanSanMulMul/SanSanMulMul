package com.sansantek.sansanmulmul.mountain.controller;

import com.sansantek.sansanmulmul.exception.InvalidTokenException;
import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.service.UserMountainService;
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
@RequestMapping("/mountain")
@Tag(name = "회원 즐겨찾기 컨트롤러", description = "회원 즐겨찾기의 관한 모든 기능 수행")
public class UserMountainController {

    private final UserService userService;
    private final UserMountainService userMountainService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/like")
    @Operation(summary = "회원 즐겨찾기 조회", description = "액세스 토큰을 사용해 회원 즐겨찾기 조회")
    public ResponseEntity<Map<String, Object>> getUserLikedMountains
            (@RequestHeader("Authorization") String accessToken) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        log.debug("user accessToken: {}", accessToken);

        try {
            String token = accessToken.substring(7);

            if (jwtTokenProvider.validateToken(token)) {
                String userProviderId = jwtTokenProvider.getUserProviderId(token);

                List<Mountain> likedMountains = userMountainService.getLikedMountains(userProviderId);

                resultMap.put("likedMountains", likedMountains);
                status = HttpStatus.OK;
            } else {
                throw new InvalidTokenException();
            }
        } catch (InvalidTokenException e) {
            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED;
        } catch (Exception e) {
            log.error("회원 즐겨찾기 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/{mountainId}")
    @Operation(summary = "회원 즐겨찾기 추가", description = "액세스 토큰을 사용해 회원 즐겨찾기 추가")
    public ResponseEntity<Map<String, Object>> addLikedMountain
            (@RequestHeader("Authorization") String accessToken,
             @PathVariable("mountainId") Long mountainId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        log.debug("user accessToken: {}", accessToken);

        try {
            String token = accessToken.substring(7);

            if (jwtTokenProvider.validateToken(token)) {
                String userProviderId = jwtTokenProvider.getUserProviderId(token);

                userMountainService.addLikedMountain(userProviderId, mountainId);

                resultMap.put("message", "산 즐겨찾기 성공");
                status = HttpStatus.OK;
            } else {
                throw new InvalidTokenException();
            }
        } catch (InvalidTokenException e) {
            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED;
        } catch (Exception e) {
            log.error("회원 즐겨찾기 추가 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @DeleteMapping("/{mountainId}")
    @Operation(summary = "회원 즐겨찾기 삭제", description = "액세스 토큰을 사용해 회원 즐겨찾기 삭제")
    public ResponseEntity<Map<String, Object>> removeLikedMountain
            (@RequestHeader("Authorization") String accessToken,
             @PathVariable("mountainId") Long mountainId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        log.debug("user accessToken: {}", accessToken);

        try {
            String token = accessToken.substring(7);

            if (jwtTokenProvider.validateToken(token)) {
                String userProviderId = jwtTokenProvider.getUserProviderId(token);

                userMountainService.removeLikedMountain(userProviderId, mountainId);

                resultMap.put("message", "즐겨찾기 제거");
                status = HttpStatus.OK;
            } else {
                throw new InvalidTokenException();
            }
        } catch (InvalidTokenException e) {
            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED;
        } catch (Exception e) {
            log.error("회원 즐겨찾기 삭제 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
