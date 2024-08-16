package com.sansantek.sansanmulmul.user.controller.follow;

import com.sansantek.sansanmulmul.config.jwt.JwtTokenProvider;
import com.sansantek.sansanmulmul.exception.follow.AlreadyFollowingException;
import com.sansantek.sansanmulmul.exception.follow.FollowNotFoundException;
import com.sansantek.sansanmulmul.exception.auth.InvalidTokenException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.dto.response.FollowResponse;
import com.sansantek.sansanmulmul.user.service.UserService;
import com.sansantek.sansanmulmul.user.service.follow.FollowService;
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
@RequestMapping("/user")
@Tag(name = "회원 팔로우 컨트롤러", description = "회원 팔로우의 관한 모든 기능 수행")
public class FollowController {

    // service
    private final UserService userService;
    private final FollowService followService;

    @GetMapping("/followers")
    @Operation(summary = "회원 팔로워 조회", description = "해당 회원의 팔로워 조회")
    public ResponseEntity<?> getFollowers
            (Authentication authentication) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {

            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 팔로워 조회
            List<FollowResponse> followers = followService.getFollowers(user.getUserId());

            status = HttpStatus.OK; // 200
            return new ResponseEntity<>(followers, status);

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            status = HttpStatus.UNAUTHORIZED; // 401

            return new ResponseEntity<>(e.getMessage(), status);
        } catch (Exception e) {

            log.error("회원 팔로워 조회 실패: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }

    @GetMapping("/{userId}/followers")
    @Operation(summary = "회원 팔로워 조회", description = "해당 회원의 팔로워 조회")
    public ResponseEntity<?> getFollowers
            (@PathVariable int userId) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 해당 사용자 가져오기
            User user = userService.getUser(userId);

            // 사용자 팔로워 조회
            List<FollowResponse> followers = followService.getFollowers(user.getUserId());

            status = HttpStatus.OK; // 200
            return new ResponseEntity<>(followers, status);

        } catch (Exception e) {

            log.error("회원 팔로워 조회 실패: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }

    @GetMapping("/followings")
    @Operation(summary = "회원 팔로잉 조회", description = "해당 회원의 팔로잉 조회")
    public ResponseEntity<?> getFollowings
            (Authentication authentication) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 팔로워 조회
            List<FollowResponse> followings = followService.getFollowings(user.getUserId());

            status = HttpStatus.OK; // 200
            return new ResponseEntity<>(followings, status);

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

    @GetMapping("/{userId}/followings")
    @Operation(summary = "회원 팔로잉 조회", description = "해당 회원의 팔로잉 조회")
    public ResponseEntity<?> getFollowings
            (@PathVariable int userId) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 해당 사용자 가져오기
            User user = userService.getUser(userId);

            // 사용자 팔로워 조회
            List<FollowResponse> followings = followService.getFollowings(user.getUserId());

            status = HttpStatus.OK; // 200
            return new ResponseEntity<>(followings, status);

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

    @PostMapping("/{userId}/follow")
    @Operation(summary = "회원 팔로우 추가", description = "해당 회원이 followUserId에 해당하는 회원 팔로우 추가")
    public ResponseEntity<?> doFollow
            (Authentication authentication,
             @RequestParam("followUserId") int followUserId) {
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 팔로우 추가
            boolean chk = followService.addFollow(user.getUserId(), followUserId);

            status = HttpStatus.OK; // 200
            return new ResponseEntity<>(chk, status);

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            status = HttpStatus.UNAUTHORIZED; // 401

            return new ResponseEntity<>(e.getMessage(), status);

        } catch (AlreadyFollowingException e) {

            log.error("error: {}", e.getMessage());
            status = HttpStatus.NOT_FOUND; // 4044

            return new ResponseEntity<>(e.getMessage(), status);

        } catch (Exception e) {

            log.error("회원 팔로우 추가 실패: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400

            return new ResponseEntity<>(e.getMessage(), status);
        }

    }

    @DeleteMapping("/unfollow")
    @Operation(summary = "회원 팔로우 취소", description = "해당 회원이 unfollowUserId에 해당하는 회원 팔로우 취소")
    public ResponseEntity<?> doUnFollow
            (Authentication authentication,
             @RequestParam("unfollowUserId") int unfollowUserId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 팔로우 추가
            boolean chk = followService.deleteFollow(user.getUserId(), unfollowUserId);

            status = HttpStatus.OK; // 200
            return new ResponseEntity<>(chk, status);

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            status = HttpStatus.UNAUTHORIZED; // 401

            return new ResponseEntity<>(e.getMessage(), status);

        } catch (FollowNotFoundException e) {

            log.error("팔로우 조회 실패: {}", e.getMessage());
            status = HttpStatus.NOT_FOUND; // 404

            return new ResponseEntity<>(e.getMessage(), status);

        } catch (Exception e) {

            log.error("error: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400

            return new ResponseEntity<>(e.getMessage(), status);

        }
    }
}
