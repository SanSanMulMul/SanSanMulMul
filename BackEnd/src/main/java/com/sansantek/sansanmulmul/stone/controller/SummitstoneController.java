package com.sansantek.sansanmulmul.stone.controller;

import com.sansantek.sansanmulmul.exception.auth.InvalidTokenException;
import com.sansantek.sansanmulmul.stone.dto.response.StoneResponse;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.service.UserService;
import com.sansantek.sansanmulmul.stone.service.SummitstoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stone")
@Tag(name = "정상석 컨트롤러", description = "회원 정상석의 관한 모든 기능 수행")
public class SummitstoneController {

    // service
    private final UserService userService;
    private final SummitstoneService summitstoneService; // user Service

    @GetMapping("/all")
    @Operation(summary = "정상석 전체 조회", description = "100대 명산의 전체 정상석 조회")
    public ResponseEntity<?> getAllSummitstones() {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {

            // 전체 정상석 리스트 조회
            List<StoneResponse> stones = summitstoneService.getStoneList();

            status = HttpStatus.OK; // 200
            return new ResponseEntity<>(stones, status);

        } catch (Exception e) {

            status = HttpStatus.NOT_FOUND; // 404
            return new ResponseEntity<>(e.getMessage(), status);

        }
    }

    @GetMapping("/detail")
    @Operation(summary = "정상석 상세 조회", description = "해당 정상석을 상세 조회")
    public ResponseEntity<?> getDetailSummitStone
            (@RequestParam int stoneId) {
        HttpStatus status = HttpStatus.ACCEPTED;

        // 해당 비석 찾기
        StoneResponse response = summitstoneService.getStone(stoneId);

        status = HttpStatus.OK; // 200;

        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/user")
    @Operation(summary = "회원 인증 정상석 조회", description = "회원이 인증한 전체 정상석 조회")
    public ResponseEntity<?> getUserStone
            (Authentication authentication) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 인증 정상석 조회
            List<StoneResponse> userStoneList = summitstoneService.getStoneListByUser(user.getUserId());

            status = HttpStatus.OK; // 200

            return new ResponseEntity<>(userStoneList, status);
        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            status = HttpStatus.UNAUTHORIZED; // 401

            return new ResponseEntity<>(e.getMessage(), status);
        } catch (Exception e) {

            log.error("회원 정상석 조회 실패: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }

    @PostMapping("/user")
    @Operation(summary = "회원 인증 정상석 추가", description = "회원이 인증한 정상석 조회")
    public ResponseEntity<?> addUserStone
            (Authentication authentication,
             @RequestParam int stoneId) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 인증 정상석 조회
            boolean chk = summitstoneService.addStone(user.getUserId(), stoneId);
            status = HttpStatus.OK; // 200

            return new ResponseEntity<>(chk, status);

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            status = HttpStatus.UNAUTHORIZED; // 401

            return new ResponseEntity<>(e.getMessage(), status);
        } catch (Exception e) {

            log.error("회원 정상석 추가 실패: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }
    
    @GetMapping("/user/chk")
    @Operation(summary = "회원 인증 정상석 확인", description = "회원이 인증한 정상석인지 확인")
    public ResponseEntity<?> chkStone
            (Authentication authentication,
             @RequestParam int mountainId) {
        HttpStatus status = HttpStatus.ACCEPTED; // 202

        try {
            // 회원 확인
            String userProviderId = authentication.getName();

            // 회원 인증 정상석 확인
            boolean chk = summitstoneService.chkUserStone(userProviderId, mountainId);
            status = HttpStatus.OK; // 200

            return new ResponseEntity<>(chk, status);
        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            status = HttpStatus.UNAUTHORIZED; // 401

            return new ResponseEntity<>(e.getMessage(), status);
        } catch (Exception e) {

            log.error("회원 인증 정상석 확인 실패: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }
}
