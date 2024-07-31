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

    // JWT
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/followers")
    @Operation(summary = "회원 팔로워 조회", description = "해당 회원의 팔로워 조회")
    public ResponseEntity<Map<String, Object>> getFollowers
        (Authentication authentication) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {

            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 팔로워 조회
            List<FollowResponse> followers = followService.getFollowers(user.getUserId());

            // JSON으로 결과 전송
            resultMap.put("followersListSize", followers.size());
            resultMap.put("followersList", followers);

            status = HttpStatus.OK; // 200

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED; // 401

        } catch (Exception e) {

            log.error("회원 팔로워 조회 실패: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400

        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/followings")
    @Operation(summary = "회원 팔로잉 조회", description = "해당 회원의 팔로잉 조회")
    public ResponseEntity<Map<String, Object>> getFollowings
        (Authentication authentication) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 사용자 팔로워 조회
            List<FollowResponse> followings = followService.getFollowings(user.getUserId());

            // JSON으로 결과 전송
            resultMap.put("followingsListSize", followings.size());
            resultMap.put("followingsList", followings);
            status = HttpStatus.OK; // 200

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED; // 401

        } catch (Exception e) {

            log.error("회원 정상석 조회 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.BAD_REQUEST; // 400

        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/follow")
    @Operation(summary = "회원 팔로우 추가", description = "해당 회원이 followUserId에 해당하는 회원 팔로우 추가")
    public ResponseEntity<Map<String, Object>> doFollow
            (Authentication authentication,
                    @RequestParam("followUserId") int followUserId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            // 토큰을 통한 userProviderId 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 팔로우 추가
            followService.addFollow(user.getUserId(), followUserId);

            // JSON으로 결과 전송
            resultMap.put("followerUserId", user.getUserId());
            resultMap.put("followingUserId", followUserId);

            status = HttpStatus.OK; // 200

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED; // 401

        } catch (AlreadyFollowingException e) {

            log.error("error: {}", e.getMessage());
            status = HttpStatus.NOT_FOUND; // 404

        } catch (Exception e) {

            log.error("회원 팔로우 추가 실패: {}", e.getMessage());
            resultMap.put("error", "An unexpected error occurred");
            status = HttpStatus.BAD_REQUEST; // 400

        }

        return new ResponseEntity<>(resultMap, status);
    }

    @DeleteMapping("/unfollow")
    @Operation(summary = "회원 팔로우 취소", description = "해당 회원이 unfollowUserId에 해당하는 회원 팔로우 취소")
    public ResponseEntity<Map<String, Object>> doUnFollow
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
            followService.deleteFollow(user.getUserId(), unfollowUserId);

            // JSON으로 결과 전송
            resultMap.put("unFollowerUserId", user.getUserId());
            resultMap.put("unFollowingUserId", unfollowUserId);

            status = HttpStatus.OK; // 200

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            resultMap.put("error", "Invalid or expired token");
            status = HttpStatus.UNAUTHORIZED; // 401

        } catch (FollowNotFoundException e) {

            log.error("팔로우 조회 실패: {}", e.getMessage());
            status = HttpStatus.NOT_FOUND; // 404

        } catch (Exception e) {

            log.error("error: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400

        }


        return new ResponseEntity<>(resultMap, status);
    }
}
