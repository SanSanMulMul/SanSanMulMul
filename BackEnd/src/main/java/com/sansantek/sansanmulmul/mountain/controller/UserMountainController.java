package com.sansantek.sansanmulmul.mountain.controller;

import com.sansantek.sansanmulmul.exception.auth.InvalidTokenException;

import com.sansantek.sansanmulmul.exception.auth.UserNotFoundException;
import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.service.UserMountainService;
import com.sansantek.sansanmulmul.user.service.UserService;
import com.sansantek.sansanmulmul.config.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    public ResponseEntity<List<Mountain>> getUserLikedMountains(Authentication authentication) {
        try {
            String userProviderId = authentication.getName();
            List<Mountain> likedMountains = userMountainService.getLikedMountains(userProviderId);
            return ResponseEntity.ok(likedMountains);
        } catch (InvalidTokenException e) {
            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        } catch (Exception e) {
            log.error("회원 즐겨찾기 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PostMapping("/{mountainId}")
    @Operation(summary = "회원 즐겨찾기 추가", description = "액세스 토큰을 사용해 회원 즐겨찾기 추가")
    public ResponseEntity<String> addLikedMountain(
            Authentication authentication,
            @PathVariable("mountainId") int mountainId) {
        try {
            String userProviderId = authentication.getName();
            userMountainService.addLikedMountain(userProviderId, mountainId);
            return new ResponseEntity<>("산 즐겨찾기 성공", HttpStatus.OK);
        } catch (InvalidTokenException e) {
            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("회원 즐겨찾기 추가 실패: {}", e.getMessage());
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{mountainId}")
    @Operation(summary = "회원 즐겨찾기 삭제", description = "액세스 토큰을 사용해 회원 즐겨찾기 삭제")
    public ResponseEntity<String> removeLikedMountain(
            Authentication authentication,
            @PathVariable("mountainId") int mountainId) {
        try {
            String userProviderId = authentication.getName();
            userMountainService.removeLikedMountain(userProviderId, mountainId);
            return new ResponseEntity<>("즐겨찾기 제거", HttpStatus.OK);
        } catch (InvalidTokenException e) {
            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("회원 즐겨찾기 삭제 실패: {}", e.getMessage());
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/like/{userId}")
    @Operation(summary = "회원 즐겨찾기 조회", description = "userId를 사용해 해당 회원의 즐겨찾기한 산 목록을 조회")
    public ResponseEntity<List<Mountain>> getUserLikedMountainsById(@PathVariable int userId) {
        try {
            List<Mountain> likedMountains = userMountainService.getLikedMountainsByUserId(userId);
            return ResponseEntity.ok(likedMountains);
        } catch (EntityNotFoundException e) {
            log.error("유저를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            log.error("회원 즐겨찾기 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

}
