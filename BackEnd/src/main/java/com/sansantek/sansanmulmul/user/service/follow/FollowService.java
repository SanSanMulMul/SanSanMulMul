package com.sansantek.sansanmulmul.user.service.follow;

import com.sansantek.sansanmulmul.exception.auth.UserNotFoundException;
import com.sansantek.sansanmulmul.exception.follow.AlreadyFollowingException;
import com.sansantek.sansanmulmul.exception.follow.FollowNotFoundException;
import com.sansantek.sansanmulmul.exception.follow.FollowOperationException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.follow.Follow;
import com.sansantek.sansanmulmul.user.dto.response.FollowResponse;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import com.sansantek.sansanmulmul.user.repository.badge.BadgeRepository;
import com.sansantek.sansanmulmul.user.repository.follow.FollowRepository;
import com.sansantek.sansanmulmul.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FollowService {

    // repository
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final BadgeRepository badgeRepository;

    @Autowired
    private EntityManager entityManager;

    // service
    private final UserService userService;

    // userId회원이 갖고 있는 팔로워 리스트 조회
    public List<FollowResponse> getFollowers(int userId) {
        List<FollowResponse> followersList = new ArrayList<>();

        // userId에 해당하는 followers 리스트 조회
        List<Follow> userFollowers = followRepository.findByFollowing_UserId(userId);

        // userFollowers에서 follower User 객체를 추출해 followersList에 저장
        for (Follow follow : userFollowers) {
            String badge =badgeRepository.findByBadgeId(follow.getFollowing().getUserStaticBadge()).get().getBadgeImage() + " " + badgeRepository.findByBadgeId(follow.getFollowing().getUserStaticBadge()).get().getBadgeName();
            FollowResponse frs = new FollowResponse(
                    follow.getFollower().getUserProfileImg(),
                    badge,
                    follow.getFollower().getUserNickname());

            followersList.add(frs);
        }

        return followersList;
    }

    // userId회원이 하고 있는 팔로우 리스트 조회
    public List<FollowResponse> getFollowings(int userId) {
        // 사용자 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException());
        
        // 반환할 팔로잉 리스트
        List<FollowResponse> followingsList = new ArrayList<>();

        // userId에 해당하는 followings 리스트 조회
        List<Follow> userFollowings = followRepository.findByFollower_UserId(userId);

        // userFollowings에서 following User 객체를 추출해 followingsList에 저장
        for (Follow follow : userFollowings){
            String badge =badgeRepository.findByBadgeId(follow.getFollowing().getUserStaticBadge()).get().getBadgeImage() + " " + badgeRepository.findByBadgeId(follow.getFollowing().getUserStaticBadge()).get().getBadgeName();
            FollowResponse frs = new FollowResponse(
                    follow.getFollowing().getUserProfileImg(), 
                    badge,
                    follow.getFollowing().getUserNickname());

            followingsList.add(frs);
        }


        return followingsList;
    }

    // userId회원이 followUserId회원을 팔로우
    @Transactional
    public boolean addFollow(int userId, int followUserId) {
        try {
            // 팔로우를 진행할 회원 정보 조회
            User follower = userService.getUser(userId);
            User following = userService.getUser(followUserId);

            // 이미 팔로우 중인지 확인
            Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowing(follower, following);
            if (existingFollow.isPresent()) {
                throw new AlreadyFollowingException("이미 팔로우 중입니다.");
            }

            // Follow 정보 생성
            Follow follow = Follow.builder()
                    .follower(follower)
                    .following(following)
                    .build();

            // Follow 객체 저장
            followRepository.saveAndFlush(follow);

            // 양방향 관계 설정
            follower.getFollowings().add(follow);
            following.getFollowers().add(follow);

            // User 객체 저장
            userRepository.save(follower);
            userRepository.save(following);

            return true;

        } catch (UserNotFoundException e) {
            // 회원 정보 조회 실패 시 사용자 정의 예외 처리
            throw new FollowNotFoundException();
        } catch (AlreadyFollowingException e) {
            // 이미 팔로우 중인 경우 예외 처리
            throw e; // 기존 예외 그대로 던짐
        } catch (Exception e) {
            // 기타 예외 처리
            throw new FollowOperationException("팔로우 처리 중 문제가 발생했습니다.", e);
        }
    }

    // userId회원이 followUserId회원을 언팔로우
    @Transactional
    public boolean deleteFollow(int userId, int followUserId) {
        try {
            // 언팔로우를 할 회원 정보 조회
            User follower = userService.getUser(userId);
            log.info("follower: " + follower.getUserNickname());

            // 언팔로우를 당할 회원 정보 조회
            User following = userService.getUser(followUserId);
            log.info("following: " + following.getUserNickname());

            // Follow 정보 찾기
            Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowing(follower, following);
            if (existingFollow.isEmpty()) {
                // 팔로우 중이 아닌 경우 예외 처리
                throw new FollowNotFoundException();
            }

            // Follow 객체 삭제
            followRepository.delete(existingFollow.get());

            // 양방향 관계 삭제
            follower.getFollowings().remove(existingFollow.get());
            following.getFollowers().remove(existingFollow.get());

            // User 객체 저장
            userRepository.save(follower);
            userRepository.save(following);

            return true;

        } catch (UserNotFoundException e) {
            // 회원 정보 조회 실패 시 사용자 정의 예외 처리
            throw new FollowOperationException("회원 정보를 찾을 수 없습니다.", e);
        } catch (FollowNotFoundException e) {
            // 팔로우 관계가 없을 때 예외 처리
            throw e; // 기존 예외 그대로 던짐
        } catch (Exception e) {
            // 기타 예외 처리
            throw new FollowOperationException("언팔로우 처리 중 문제가 발생했습니다.", e);
        }
    }
}
