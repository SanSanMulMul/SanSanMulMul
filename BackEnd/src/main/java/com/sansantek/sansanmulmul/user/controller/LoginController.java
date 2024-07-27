package com.sansantek.sansanmulmul.user.controller;

import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.badge.UserBadge;
import com.sansantek.sansanmulmul.user.dto.request.SignUpUserRequest;
import com.sansantek.sansanmulmul.user.dto.response.KakaoUserInfoResponse;
import com.sansantek.sansanmulmul.user.service.BadgeService;
import com.sansantek.sansanmulmul.user.service.KakaoService;
import com.sansantek.sansanmulmul.user.service.TokenService;
import com.sansantek.sansanmulmul.user.service.UserService;
import com.sansantek.sansanmulmul.config.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "로그인, 회원가입 컨트롤러", description = "회원의 로그인과 회원가입 기능 수행")
public class LoginController {

    // service
    private final UserService userService;
    private final KakaoService kakaoService;
    private final TokenService tokenService;
    private final BadgeService badgeService;

    // JWT
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/login")
    @Operation(summary = "로그인", description = "카카오 소셜 로그인 + JWT 토큰")
    public ResponseEntity<?> login
            (@RequestParam("code") String code) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        // 프론트에서 받은 인가로 토큰 받기 시행
        String kakaoAccessToken = kakaoService.getAccessTokenFromKakao(code);

        // 회원 정보 가져오기
        KakaoUserInfoResponse userInfo = kakaoService.getUserInfo(kakaoAccessToken);
        String id = String.valueOf(userInfo.getId()); // 카카오에서 제공하는 아이디(id) == DB 카카오 제공 아이디(userProviderId)
        String nickName = userInfo.getKakaoAccount().getProfile().getNickName(); // 카카오에서 제공하는 닉네임 -> User name으로 들어가야함 !!

        // 카카오에서 제공받은 아이디가 DB에 저장되어 있지 않다면?
        if (userService.isExistsUser(id)) { // DB에 저장되어 있는 회원인 경우
            // 회원 정보 가져오기
            User loginUser = userService.getUser(id);

            // 토큰 발급
            List<String> tokens = provideToken(id);
            String accessToken = tokens.get(0);
            String refreshToken = tokens.get(1);

            // JSON 으로 token 전달
            resultMap.put("userInfo", loginUser);
            resultMap.put("access-token", accessToken);
            resultMap.put("refresh-token", refreshToken);

            // 상태 변경
            status = HttpStatus.OK; // 200

        } else { // DB에 저장되어 있지 않은 회원인 경우

            // 카카오에서 제공받은 아이디와 닉네임을 JSON으로 프론트에게 전달
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

            // 로그인 + 토큰 발급까지 완료하기
            List<String> tokens = provideToken(user.getUserProviderId());
            String accessToken = tokens.get(0);
            String refreshToken = tokens.get(1);

            // 기본 칭호(badge_id = 1) 등록하기
            badgeService.setBasicBadge(user.getUserId());

            // JSON 으로 token 전달
            resultMap.put("userId", user.getUserId());
            resultMap.put("access-token", accessToken);
            resultMap.put("refresh-token", refreshToken);

            // 상태 변경
            status = HttpStatus.CREATED; // 201

        } catch (Exception e) {

            log.error("회원가입 실패");
            log.debug("sign-up user : {}", request);
            status = HttpStatus.BAD_REQUEST; // 400

        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);

    }

    // 토큰 생성
    private List<String> provideToken(String userProviderId) {
        // 반환할 토큰들 저장
        List<String> tokens = new ArrayList<>();

        // 2개 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(userProviderId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userProviderId);

        // 2개 토큰 확인
        log.debug("accessToken: {}", accessToken);
        log.debug("refreshToken: {}", refreshToken);

        // 발급받은 refresh token 을 DB에 저장
        tokenService.saveRefreshToken(userProviderId, refreshToken);

        // 리스트에 토큰 추가
        tokens.add(accessToken);
        tokens.add(refreshToken);

        // 토큰 리스트 반환
        return tokens;
    }
}
