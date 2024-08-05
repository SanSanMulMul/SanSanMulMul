package com.sansantek.sansanmulmul.user.controller.style;

import com.sansantek.sansanmulmul.config.jwt.JwtTokenProvider;
import com.sansantek.sansanmulmul.exception.auth.InvalidTokenException;
import com.sansantek.sansanmulmul.exception.style.AlreadyStyleException;
import com.sansantek.sansanmulmul.exception.style.StyleNotFoundException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.dto.request.UpdateUserHikingStyleRequest;
import com.sansantek.sansanmulmul.user.dto.response.UserStyleResponse;
import com.sansantek.sansanmulmul.user.service.UserService;
import com.sansantek.sansanmulmul.user.service.style.UserStyleService;
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
@RequestMapping("/user/style")
@Tag(name = "회원 등산 스타일 컨트롤러", description = "회원 등산 스타일의 관한 모든 기능 수행")
public class UserStyleController {

    // service
    private final UserService userService;
    private final UserStyleService userStyleService;

    @GetMapping
    @Operation(summary = "회원 등산 스타일 조회", description = "해당 회원의 선택된 등산 스타일 조회")
    public ResponseEntity<?> getHikingStyles
            (Authentication authentication) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 등산 스타일 조회
            List<Integer> userHikingStyleList = userStyleService.getStyleList(user.getUserId());

            status = HttpStatus.OK; // 200

            return new ResponseEntity<>(userHikingStyleList, status);
        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            status = HttpStatus.UNAUTHORIZED; // 401

            return new ResponseEntity<>(e.getMessage(), status);

        } catch (Exception e) {

            log.error("회원 등산 스타일 조회 실패: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400

            return new ResponseEntity<>(e.getMessage(), status);
        }

    }

    @PutMapping
    @Operation(summary = "회원 등산 스타일 수정", description = "해당 회원의 등산 스타일 수정")
    public ResponseEntity<?> updateHIkingStyle
            (Authentication authentication,
             @RequestBody UpdateUserHikingStyleRequest request) {

        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 등산 스타일 수정
            boolean response = userStyleService.updateUserHikingStyle(user.getUserId(), request);

            status = HttpStatus.OK; // 200

            return new ResponseEntity(response, status);

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            status = HttpStatus.UNAUTHORIZED; // 401


            return new ResponseEntity(e.getMessage(), status);
        }
    }

    @PostMapping
    @Operation(summary = "회원 등산 스타일 추가", description = "해당 회원의 등산 스타일 추가")
    public ResponseEntity<Map<String, Object>> addHikingStyle
            (Authentication authentication,
             @RequestParam int hikingStyleId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 등산 스타일 추가
            userStyleService.addStyle(user.getUserId(), hikingStyleId);

            // JSON으로 결과 전송
            resultMap.put("userId", user.getUserId());
            resultMap.put("userProviderId", user.getUserProviderId());
            resultMap.put("addHikingStyleId", hikingStyleId);
            resultMap.put("addHikingStyleName", userStyleService.getStyleName(hikingStyleId));

            status = HttpStatus.OK;
        } catch (AlreadyStyleException e) {
            log.error("회원 등산 스타일 추가 중복: {}", e.getMessage());
            status = HttpStatus.NOT_FOUND; // 404
        } catch (InvalidTokenException e) {
            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED; // 401
        } catch (Exception e) {
            log.error("회원 등산 스타일 추가 실패: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @DeleteMapping
    @Operation(summary = "회원 등산 스타일 제거", description = "해당 회원의 등산 스타일 제거")
    public ResponseEntity<Map<String, Object>> deleteHikingStyle
            (Authentication authentication,
             @RequestParam int hikingStyleId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 등산 스타일 제거
            userStyleService.deleteStyle(user.getUserId(), hikingStyleId);

            // JSON으로 결과 전송
            resultMap.put("userId", user.getUserId());
            resultMap.put("userProviderId", user.getUserProviderId());
            resultMap.put("deleteHikingStyleId", hikingStyleId);
            resultMap.put("deleteHikingStyleName", userStyleService.getStyleName(hikingStyleId));

            status = HttpStatus.OK; // 200
        } catch (StyleNotFoundException e) {

            log.error("회원 등산 스타일 이미 제거 완료 : {}", e.getMessage());
            status = HttpStatus.NOT_FOUND; // 404

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED; // 401

        } catch (Exception e) {

            log.error("회원 등산 스타일 제거 실패: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400

        }

        return new ResponseEntity<>(resultMap, status);
    }
}
