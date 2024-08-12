package com.sansantek.sansanmulmul.user.controller;

import com.sansantek.sansanmulmul.exception.auth.UserNotFoundException;
import com.sansantek.sansanmulmul.exception.user.UserDeletionException;
import com.sansantek.sansanmulmul.exception.user.UserUpdateException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.dto.request.UpdateUserRequest;
import com.sansantek.sansanmulmul.user.dto.response.MyPageResponse;
import com.sansantek.sansanmulmul.user.dto.response.UserInfoResponse;
import com.sansantek.sansanmulmul.user.service.UserService;
import com.sansantek.sansanmulmul.config.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/nickname")
    @Operation(summary = "닉네임 중복 확인", description = "회원가입 시 입력한 닉네임을 포함해 중복 확인")
    public ResponseEntity<?> chkNickname
            (@RequestParam String userNickname) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 사용자 닉네임 중복 확인
            if (userService.isExistsUserNickname(userNickname)) {
                status = HttpStatus.CONFLICT; // 409

                return new ResponseEntity<>(false, status);
            } else {
                status = HttpStatus.OK; // 200

                return new ResponseEntity<>(true, status);
            }

        } catch (Exception e) {

            log.error("닉네임 중복 확인 실패");
            status = HttpStatus.BAD_REQUEST; // 400

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }

    @GetMapping("/chknick")
    @Operation(summary = "닉네임 중복 확인", description = "마이 페이지 편집 시 입력한 닉네임을 포함해 중복 확인")
    public ResponseEntity<?> chkNicknameEdit
            (Authentication authentication,
                    @RequestParam String userNickname) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 사용자 닉네임 중복 확인
            if (userService.isExistsUserNicknameNotMe(userNickname, userProviderId)) {
                status = HttpStatus.CONFLICT; // 409

                return new ResponseEntity<>(false, status);
            } else {
                status = HttpStatus.OK; // 200

                return new ResponseEntity<>(true, status);
            }

        } catch (Exception e) {

            log.error("닉네임 중복 확인 실패");
            status = HttpStatus.BAD_REQUEST; // 400

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }

    @GetMapping("/info")
    @Operation(summary = "회원 정보 조회", description = "액세스 토큰을 사용해 회원 정보 조회")
    public ResponseEntity<?> getUserInfo
            (Authentication authentication) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 정보 조회
            UserInfoResponse user = userService.getUserResponse(userProviderId);
            status = HttpStatus.OK;

            log.debug("userInfo : {}", user);
            return new ResponseEntity<>(user, status);
        } catch (Exception e) {
            log.error("토큰 유효성 확인 실패");
            status = HttpStatus.UNAUTHORIZED;

            return new ResponseEntity<>(e, status);
        }
    }

    @GetMapping("/{userId}/info")
    @Operation(summary = "회원 정보 조회", description = "액세스 토큰을 사용해 회원 정보 조회")
    public ResponseEntity<?> getUserInfo
            (@PathVariable int userId) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {// 해당 사용자 정보 조회
            UserInfoResponse user = userService.getUserResponse(userId);
            status = HttpStatus.OK;

            log.debug("userInfo : {}", user);
            return new ResponseEntity<>(user, status);
        } catch (Exception e) {
            log.error("토큰 유효성 확인 실패");
            status = HttpStatus.UNAUTHORIZED;

            return new ResponseEntity<>(e, status);
        }
    }

    @PatchMapping(value = "/info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원 정보 수정", description = "액세스 토큰을 사용해 회원 정보 수정")
    public ResponseEntity<?> updateUserInfo(
            Authentication authentication,
            @RequestPart(value="updateUserRequest") UpdateUserRequest updateUserRequest,
            @RequestPart(value="image")MultipartFile image) {

        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 사용자 정보 수정
            boolean chk = userService.updateUser(userProviderId, updateUserRequest, image);
            status = HttpStatus.OK;

            return new ResponseEntity<>(chk, status);

        } catch (UserNotFoundException e) {
            log.error("회원 정보 수정 실패 - 사용자 없음: {}", e.getMessage());
            status = HttpStatus.NOT_FOUND;

            return new ResponseEntity<>(e.getMessage(), status);
        } catch (UserUpdateException e) {
            log.error("회원 정보 수정 실패 - 처리 중 오류: {}", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;

            return new ResponseEntity<>(e.getMessage(), status);
        } catch (Exception e) {
            log.error("회원 정보 수정 실패 - 인증 오류: {}", e.getMessage());
            status = HttpStatus.UNAUTHORIZED;

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "액세스 토큰을 사용해 회원 탈퇴")
    public ResponseEntity<?> deleteUser
            (Authentication authentication) {

        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 정보 삭제
            boolean chk = userService.deleteUser(userProviderId);

            status = HttpStatus.OK; // 200

            return new ResponseEntity<>(chk, status);

        }catch (UserDeletionException e) {

            log.error("회원 정보 삭제 실패: {}", e.getMessage());
            status = HttpStatus.NOT_FOUND; // 404

            return new ResponseEntity<>(e.getMessage(), status);
        } catch (Exception e) {

            log.error("회원 토큰 인증 실패 : {}", e.getMessage());
            status = HttpStatus.UNAUTHORIZED; // 401

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }

    @GetMapping("/mypage")
    @Operation(summary = "마이페이지 내 회원 정보 조회", description = "액세스 토큰을 사용해 마이페이지 내 필요한 회원 정보조회")
    public ResponseEntity<?> getMypageInfo
            (Authentication authentication) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 정보 조회
            MyPageResponse user = userService.getMyPageResponse(userProviderId);
            status = HttpStatus.OK;

            log.debug("userInfo : {}", user);
            return new ResponseEntity<>(user, status);
        } catch (Exception e) {
            log.error("토큰 유효성 확인 실패");
            status = HttpStatus.UNAUTHORIZED;

            return new ResponseEntity<>(e, status);
        }
    }
}
