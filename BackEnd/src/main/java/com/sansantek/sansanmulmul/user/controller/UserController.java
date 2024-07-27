package com.sansantek.sansanmulmul.user.controller;

import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.dto.request.UpdateUserRequest;
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
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "회원 컨트롤러", description = "회원 정보관련 기능 수행")
public class UserController {

    // service
    private final UserService userService;

    // JWT
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/nickname")
    @Operation(summary = "닉네임 중복 확인", description = "액세스 토큰을 사용해 회원 정보 조회 후 닉네임 중복 확인")
    public ResponseEntity<Map<String, Object>> chkNickname
            (@RequestHeader("Authorization")String accessToken) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        log.debug("user accessToken: {}", accessToken);

        try {
            // Authorization 헤더에서 "Bearer " 접두사를 제거
            String token = accessToken.substring(7);

            // 액세스 토큰 유효성 검증
            if (jwtTokenProvider.checkToken(token)) {
                String userProviderId = jwtTokenProvider.getUserProviderId(token);
                // 해당 사용자 정보 조회
                User user = userService.getUser(userProviderId);
                String userNickname = user.getUserNickname();
                log.debug("userInfo : {}", userNickname);

                // 사용자 닉네임 중복 확인
                if (userService.isExistsUserNickname(userNickname)) {
                    // 해당 닉네임 이미 존재
                    resultMap.put("userNickname", userNickname);
                    resultMap.put("message", "해당 닉네임을 가진 사용자가 존재합니다.");
                    status = HttpStatus.CONFLICT; // 409
                } else {
                    resultMap.put("userInfo", user);
                    status = HttpStatus.OK; // 200
                }

            } else {
                throw new Exception("Invalid Token");
            }
        } catch (Exception e) {
            log.error("토큰 유효성 확인 실패");
            status = HttpStatus.UNAUTHORIZED; // 401
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/info")
    @Operation(summary = "회원 정보 조회", description = "액세스 토큰을 사용해 회원 정보 조회")
    public ResponseEntity<Map<String, Object>> getUserInfo
            (@RequestHeader("Authorization")String accessToken) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        log.debug("user accessToken: {}", accessToken);

        try {
            // Authorization 헤더에서 "Bearer " 접두사를 제거
            String token = accessToken.substring(7);

            // 액세스 토큰 유효성 검증
            if (jwtTokenProvider.checkToken(token)) {
                String userProviderId = jwtTokenProvider.getUserProviderId(token);
                // 해당 사용자 정보 조회
                User user = userService.getUser(userProviderId);

                log.debug("userInfo : {}", user);
                resultMap.put("userInfo", user);
                status = HttpStatus.OK;

            } else {
                throw new Exception("Invalid Token");
            }
        } catch (Exception e) {
            log.error("토큰 유효성 확인 실패");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PatchMapping("/info")
    @Operation(summary = "회원 정보 수정", description = "액세스 토큰을 사용해 회원 정보 수정")
    public ResponseEntity<Map<String, Object>> updateUserInfo
            (@RequestHeader("Authorization")String accessToken,
             @RequestBody UpdateUserRequest updateUserRequest) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        log.debug("user accessToken: {}", accessToken);

        try {
            // Authorization 헤더에서 "Bearer " 접두사를 제거
            String token = accessToken.substring(7);

            // 액세스 토큰 유효성 검증
            if (jwtTokenProvider.checkToken(token)) {
                String userProviderId = jwtTokenProvider.getUserProviderId(token);

                // 해당 사용자 정보 수정
                User updatedUser = userService.updateUser(userProviderId, updateUserRequest);

                resultMap.put("userInfo", updatedUser);
                status = HttpStatus.OK;

            } else {
                throw new Exception("Invalid Token");
            }
        } catch (Exception e) {
            log.error("회원 정보 수정 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(resultMap, status);
    }
}
