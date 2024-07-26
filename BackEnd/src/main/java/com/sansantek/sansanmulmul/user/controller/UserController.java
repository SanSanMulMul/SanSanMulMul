package com.sansantek.sansanmulmul.user.controller;

import com.sansantek.sansanmulmul.config.JwtTokenProvider;
import com.sansantek.sansanmulmul.exception.InvalidTokenException;
import com.sansantek.sansanmulmul.exception.UserNotFoundException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.dto.request.SignUpUserRequest;
import com.sansantek.sansanmulmul.user.dto.request.UpdateUserRequest;
import com.sansantek.sansanmulmul.user.dto.response.KakaoUserInfoResponse;
import com.sansantek.sansanmulmul.user.service.BadgeService;
import com.sansantek.sansanmulmul.user.service.KakaoService;
import com.sansantek.sansanmulmul.user.service.UserService;
import com.sansantek.sansanmulmul.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/user")
@Tag(name = "회원 컨트롤러", description = "회원의 관한 모든 기능 수행")
public class UserController {

    // service
    private final UserService userService;
    private final KakaoService kakaoService;
    private final BadgeService badgeService;

    // JWT
    private final JWTUtil jwtUtil;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/login")
    @Operation(summary = "로그인", description = "카카오 소셜 로그인 + JWT 토큰")
    public ResponseEntity<?> callback
            (@RequestParam("code") String code) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        // 프론트에서 받은 인가로 토큰 받기 시행 !
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        // 회원 정보 가져오기
        KakaoUserInfoResponse userInfo = kakaoService.getUserInfo(accessToken);
        String id = String.valueOf(userInfo.getId()); // 카카오에서 제공하는 아이디
        String nickName = userInfo.getKakaoAccount().getProfile().getNickName(); // 카카오에서 제공하는 닉네임 -> User name으로 들어가야함 !!
        String profileImageUrl = userInfo.getKakaoAccount().getProfile().getProfileImageUrl(); // 카카오에서 제공하는 프로필사진 url

        // 카카오에서 제공받은 아이디가 DB에 저장되어 있지 않다면?
        if (userService.isExistsUser(id)) {

            // DB에 저장되어 있는 회원인 경우
            User loginUser = userService.getUser(id);

            // Token 2개 발급
            accessToken = jwtUtil.createAccessToken(loginUser.getUserProviderId());
            String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserProviderId());
            log.debug("access token : {}", accessToken);
            log.debug("refresh token : {}", refreshToken);

            // 발급받은 refresh token 을 DB에 저장
            userService.saveRefreshToken(loginUser.getUserProviderId(), refreshToken);

            // JSON 으로 token 전달
            resultMap.put("userInfo", loginUser);
            resultMap.put("access-token", accessToken);
            resultMap.put("refresh-token", refreshToken);

            // 상태 변경
            status = HttpStatus.OK; // 200

        } else { // DB에 저장되어 있지 않은 회원인 경우

            // 카카오에서 제공받은 아이디와 닉네임을 JSON으로 전달
            resultMap.put("userProviderId", id);
            resultMap.put("userName", nickName);
            resultMap.put("message", "userProviderId와 userName을 사용해서 다른 정보들도 함께 /signup으로 POST요청 해주세요 !");

            // 상태 변경
            status = HttpStatus.NO_CONTENT; // 204
        }


        return new ResponseEntity<>(resultMap, status);

    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입 + 카카오 소셜 로그인 + JWT 토큰")
    public ResponseEntity<Map<String, Object>> signUp
            ( @Valid @RequestBody SignUpUserRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            User user = userService.signUpUser(request);
            log.debug("회원가입 성공");
            log.debug("sign-up user : {}", user);

            // 로그인까지 완료하기
            // Token 2개 발급
            String accessToken = jwtUtil.createAccessToken(user.getUserProviderId());
            String refreshToken = jwtUtil.createRefreshToken(user.getUserProviderId());
            log.debug("access token : {}", accessToken);
            log.debug("refresh token : {}", refreshToken);

            // 발급받은 refresh token 을 DB에 저장
            userService.saveRefreshToken(user.getUserProviderId(), refreshToken);

            // JSON 으로 token 전달
            resultMap.put("userInfo", user);
            resultMap.put("access-token", accessToken);
            resultMap.put("refresh-token", refreshToken);

            // 상태 변경
            status = HttpStatus.CREATED; // 201

        } catch (Exception e) {
            log.error("회원가입 실패");
            log.debug("sign-up user : {}", request);
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);

    }

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
            if (jwtTokenProvider.validateToken(token)) {
                String userProviderId = jwtTokenProvider.getUserIdFromToken(token);
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
            if (jwtTokenProvider.validateToken(token)) {
                String userProviderId = jwtTokenProvider.getUserIdFromToken(token);
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
            if (jwtTokenProvider.validateToken(token)) {
                String userProviderId = jwtTokenProvider.getUserIdFromToken(token);

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
