package com.sansantek.sansanmulmul.user.controller.login;

import com.sansantek.sansanmulmul.config.jwt.JwtToken;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.dto.request.SignUpUserRequest;
import com.sansantek.sansanmulmul.user.dto.response.KakaoUserInfoResponse;
import com.sansantek.sansanmulmul.user.service.badge.BadgeService;
import com.sansantek.sansanmulmul.user.service.login.KakaoService;
import com.sansantek.sansanmulmul.user.service.login.TokenService;
import com.sansantek.sansanmulmul.user.service.UserService;
import com.sansantek.sansanmulmul.user.service.style.UserStyleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "로그인, 회원가입 컨트롤러", description = "회원의 로그인과 회원가입 기능 수행")
public class LoginController {

    // service
    private final UserService userService;
    private final BadgeService badgeService;
    private final UserStyleService userStyleService;
    private final TokenService tokenService;
    private final KakaoService kakaoService;

    @GetMapping("/login")
    @Operation(summary = "로그인", description = "카카오 id로 회원 인증 + JWT 토큰 발급")
    public ResponseEntity<?> login
//            (@RequestParam("userProviderId") String userProviderId)
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
//        if (userService.isExistsUser(userProviderId)) { // DB에 저장되어 있는 회원인 경우
//            // 회원 정보 가져오기
//            User loginUser = userService.getUser(userProviderId);
//            String password = "";
//            log.info("userProviderId: {}", userProviderId);
        if (userService.isExistsUser(id)) { // DB에 저장되어 있는 회원인 경우
            // 회원 정보 가져오기
            User loginUser = userService.getUser(id);
            String password = "";
            log.info("userProviderId: {}", id);

            // 토큰 인증 기반 로그인 수행
            JwtToken jwtToken = tokenService.generateToken(loginUser.getUserProviderId(), password);

            // 토큰 저장
            tokenService.saveRefreshToken(loginUser.getUserProviderId(), jwtToken.getRefreshToken());

            // 토큰 넘겨주기
            resultMap.put("accessToken", jwtToken.getAccessToken());
            resultMap.put("refreshToken", jwtToken.getRefreshToken());

            // 상태 변경
            status = HttpStatus.OK; // 200

        } else { // DB에 저장되어 있지 않은 회원인 경우
            resultMap.put("message", "userProviderId+userName+다른 정보 /signup으로 POST요청");

            // 상태 변경
            status = HttpStatus.NO_CONTENT; // 204
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/login/{userProviderId}")
    @Operation(summary = "로그인", description = "카카오 id로 회원 인증 + JWT 토큰 발급")
    public ResponseEntity<?> doLogin
            (@PathVariable("userProviderId") String userProviderId){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        // 카카오에서 제공받은 아이디가 DB에 저장되어 있지 않다면?
        if (userService.isExistsUser(userProviderId)) { // DB에 저장되어 있는 회원인 경우
            // 회원 정보 가져오기
            User loginUser = userService.getUser(userProviderId);
            String password = "";
            log.info("userProviderId: {}", userProviderId);

            // 토큰 인증 기반 로그인 수행
            JwtToken jwtToken = tokenService.generateToken(loginUser.getUserProviderId(), password);

            // 토큰 저장
            tokenService.saveRefreshToken(loginUser.getUserProviderId(), jwtToken.getRefreshToken());

            // 토큰 넘겨주기
            resultMap.put("accessToken", jwtToken.getAccessToken());
            resultMap.put("refreshToken", jwtToken.getRefreshToken());

            // 상태 변경
            status = HttpStatus.OK; // 200

        } else { // DB에 저장되어 있지 않은 회원인 경우
            resultMap.put("message", "userProviderId+userName+다른 정보 /signup으로 POST요청");

            // 상태 변경
            status = HttpStatus.NO_CONTENT; // 204
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입 + JWT 토큰 발급")
    public ResponseEntity<Map<String, Object>> signUp(@Valid @RequestBody SignUpUserRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 회원가입 진행
            String password = "";
            User user = userService.signUp(request, password);
            log.info("회원가입 성공");
            log.info("sign-up user : {}", user);

            // 로그인 + 토큰 발급까지 완료하기
            JwtToken jwtToken = tokenService.generateToken(user.getUserProviderId(), password);

            // 기본 칭호(badge_id = 1) 등록하기
            log.info("기본 칭호 등록");
            badgeService.setBasicBadge(user.getUserId());

            // 등산 스타일 추가
            for (int hikingStyleId : request.getUserStyles())
                userStyleService.addStyle(user.getUserId(), hikingStyleId);

            // JSON 으로 token 전달
            resultMap.put("userId", user.getUserId());
            resultMap.put("accessToken", jwtToken.getAccessToken());
            resultMap.put("refreshToken", jwtToken.getRefreshToken());

            // 상태 변경
            status = HttpStatus.CREATED; // 201

        } catch (UsernameNotFoundException e) {

            log.info("회원이 존재하지 않음: {}", e.getMessage());
            log.error(e.getMessage());
            status = HttpStatus.NOT_FOUND; // 404

        } catch (AuthenticationException e) {

            log.info("회원 인증 실패: {}", e.getMessage());
            log.error(e.getMessage());
            status = HttpStatus.UNAUTHORIZED; // 401

        } catch (Exception e) {

            log.error("회원 가입 실패: {}", e.getMessage());
            log.info("sign-up user : {}", request);
            status = HttpStatus.BAD_REQUEST; // 400

        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @GetMapping("/token")
    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 검증해 새로운 액세스 토큰 발급")
    public ResponseEntity<Map<String, Object>> getToken(Authentication authentication) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 토큰 발급
            String password = "";
            JwtToken jwtToken = tokenService.generateToken(userProviderId, password);

            // JSON 으로 token 전달
            resultMap.put("accessToken", jwtToken.getAccessToken());
            resultMap.put("refreshToken", jwtToken.getRefreshToken());

            status = HttpStatus.OK; // 200

        } catch (Exception e) {

            log.error("회원 인증 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED; // 401

        }

        return new ResponseEntity<>(resultMap, status);
    }

}
