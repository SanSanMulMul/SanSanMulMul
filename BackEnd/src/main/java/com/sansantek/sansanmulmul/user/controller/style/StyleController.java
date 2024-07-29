package com.sansantek.sansanmulmul.user.controller.style;

import com.sansantek.sansanmulmul.config.jwt.JwtTokenProvider;
import com.sansantek.sansanmulmul.exception.auth.InvalidTokenException;
import com.sansantek.sansanmulmul.exception.style.AlreadyStyleException;
import com.sansantek.sansanmulmul.exception.style.StyleNotFoundException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.style.HikingStyle;
import com.sansantek.sansanmulmul.user.repository.style.HikingStyleRepository;
import com.sansantek.sansanmulmul.user.service.UserService;
import com.sansantek.sansanmulmul.user.service.style.StyleService;
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
@RequestMapping("/user")
@Tag(name = "회원 등산 스타일 컨트롤러", description = "회원 등산 스타일의 관한 모든 기능 수행")
public class StyleController {

    // service
    private final UserService userService;
    private final StyleService styleService;

    // JWT
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/style")
    @Operation(summary = "회원 등산 스타일 조회", description = "해당 회원의 선택된 등산 스타일 조회")
    public ResponseEntity<Map<String, Object>> getHikingStyles
            (@RequestParam String userProviderId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 등산 스타일 조회
            List<String> userHikingStyleList = styleService.getStyleList(user.getUserId());

            // JSON으로 결과 전송
            resultMap.put("userHikingStyleList Size", userHikingStyleList.size());
            for (int i = 0; i < userHikingStyleList.size(); i++)
                resultMap.put("Style Name" + (i+1), userHikingStyleList.get(i));


            status = HttpStatus.OK;
        } catch (InvalidTokenException e) {
            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED; // 401
        } catch (Exception e) {
            log.error("회원 등산 스타일 조회 실패: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/style")
    @Operation(summary = "회원 등산 스타일 추가", description = "해당 회원에 등산 스타일 추가")
    public ResponseEntity<Map<String, Object>> addHikingStyle
            (@RequestParam String userProviderId,
             @RequestParam int hikingStyleId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 등산 스타일 추가
            styleService.addStyle(user.getUserId(), hikingStyleId);

            // JSON으로 결과 전송
            resultMap.put("userId", user.getUserId());
            resultMap.put("userProviderId", user.getUserProviderId());
            resultMap.put("Add hikingStyleId", hikingStyleId);
            resultMap.put("Add hikingStyleName", styleService.getStyleName(hikingStyleId));

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

    @DeleteMapping("/style")
    @Operation(summary = "회원 등산 스타일 제거", description = "해당 회원에 등산 스타일 제거")
    public ResponseEntity<Map<String, Object>> deleteHikingStyle
            (@RequestParam String userProviderId,
             @RequestParam int hikingStyleId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 등산 스타일 제거
            styleService.deleteStyle(user.getUserId(), hikingStyleId);

            // JSON으로 결과 전송
            resultMap.put("userId", user.getUserId());
            resultMap.put("userProviderId", user.getUserProviderId());
            resultMap.put("hikingStyleId", hikingStyleId);

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
