package com.sansantek.sansanmulmul.user.controller;

import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.dto.request.UpdateUserRequest;
import com.sansantek.sansanmulmul.user.service.UserService;
import com.sansantek.sansanmulmul.config.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "회원 정보 컨트롤러", description = "회원 정보관련 기능 수행")
public class UserController {

    // service
    private final UserService userService;

    // JWT
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/nickname")
    @Operation(summary = "닉네임 중복 확인", description = "액세스 토큰을 사용해 회원 정보 조회 후 닉네임 중복 확인")
    public ResponseEntity<Map<String, Object>> chkNickname
            (@RequestParam String userNickname) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 사용자 닉네임 중복 확인
            if (userService.isExistsUserNickname(userNickname)) {

                // 해당 닉네임 이미 존재
                resultMap.put("userNickname", userNickname);
                resultMap.put("message", "해당 닉네임을 가진 사용자가 존재합니다.");
                status = HttpStatus.CONFLICT; // 409

            } else {

                resultMap.put("userNickname", userNickname);
                resultMap.put("message", "사용가능한 닉네임입니다.");
                status = HttpStatus.OK; // 200

            }

        } catch (Exception e) {

            log.error("닉네임 중복 확인 실패");
            status = HttpStatus.BAD_REQUEST; // 400

        }
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/info")
    @Operation(summary = "회원 정보 조회", description = "액세스 토큰을 사용해 회원 정보 조회")
    public ResponseEntity<Map<String, Object>> getUserInfo
            (Authentication authentication) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 정보 조회
            User user = userService.getUser(userProviderId);

            log.debug("userInfo : {}", user);
            resultMap.put("userId", user.getUserId());
            resultMap.put("userProviderId", userProviderId);
            resultMap.put("userName", user.getUserName());
            resultMap.put("userNickname", user.getUserNickname());
            resultMap.put("userGender", user.getUserGender());
            resultMap.put("userProfileImg", user.getUserProfileImg());
            resultMap.put("userBirth", user.getUserBirth());
            resultMap.put("userStaticBadge", user.getUserStaticBadge());
            resultMap.put("userTotalLength", user.getUserTotalLength());
            resultMap.put("userTotalElevation", user.getUserTotalElevation());
            resultMap.put("userTotalSteps", user.getUserTotalSteps());
            resultMap.put("userTotalKcal", user.getUserTotalKcal());
            resultMap.put("userTotalHiking", user.getUserTotalHiking());
            resultMap.put("userStoneCount", user.getUserStoneCount());
            resultMap.put("userIsAdmin", user.isUserIsAdmin());

            status = HttpStatus.OK;

        } catch (Exception e) {
            log.error("토큰 유효성 확인 실패");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PatchMapping("/info")
    @Operation(summary = "회원 정보 수정", description = "액세스 토큰을 사용해 회원 정보 수정")
    public ResponseEntity<Map<String, Object>> updateUserInfo
            (Authentication authentication,
             @RequestBody UpdateUserRequest updateUserRequest) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 정보 수정
            User updatedUser = userService.updateUser(userProviderId, updateUserRequest);

            resultMap.put("userId", updatedUser.getUserId());
            resultMap.put("userProviderId", userProviderId);
            resultMap.put("userName", updatedUser.getUserName());
            resultMap.put("userNickname", updatedUser.getUserNickname());
            resultMap.put("userGender", updatedUser.getUserGender());
            resultMap.put("userProfileImg", updatedUser.getUserProfileImg());
            resultMap.put("userBirth", updatedUser.getUserBirth());
            resultMap.put("userStaticBadge", updatedUser.getUserStaticBadge());
            resultMap.put("userTotalLength", updatedUser.getUserTotalLength());
            resultMap.put("userTotalElevation", updatedUser.getUserTotalElevation());
            resultMap.put("userTotalSteps", updatedUser.getUserTotalSteps());
            resultMap.put("userTotalKcal", updatedUser.getUserTotalKcal());
            resultMap.put("userTotalHiking", updatedUser.getUserTotalHiking());
            resultMap.put("userStoneCount", updatedUser.getUserStoneCount());
            resultMap.put("userIsAdmin", updatedUser.isUserIsAdmin());
            status = HttpStatus.OK;

        } catch (Exception e) {
            log.error("회원 정보 수정 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(resultMap, status);
    }
}
