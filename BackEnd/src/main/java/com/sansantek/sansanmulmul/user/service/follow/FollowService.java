package com.sansantek.sansanmulmul.user.service.follow;

import com.sansantek.sansanmulmul.exception.follow.AlreadyFollowingException;
import com.sansantek.sansanmulmul.exception.follow.FollowNotFoundException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.follow.Follow;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
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

    @Autowired
    private EntityManager entityManager;

    // service
    private final UserService userService;

    // userId회원이 갖고 있는 팔로워 리스트 조회
    public List<User> getFollowers(int userId) {
        List<User> followersList = new ArrayList<>();

        // userId에 해당하는 followers 리스트 조회
        List<Follow> userFollowers = followRepository.findByFollowing_UserId(userId);

        // userFollowers에서 follower User 객체를 추출해 followersList에 저장
        for (Follow follow : userFollowers)
            followersList.add(follow.getFollower());


        return followersList;
    }

    // userId회원이 하고 있는 팔로우 리스트 조회
    public List<User> getFollowings(int userId) {
        List<User> followingsList = new ArrayList<>();

        // userId에 해당하는 followings 리스트 조회
        List<Follow> userFollowings = followRepository.findByFollower_UserId(userId);

        // userFollowings에서 following User 객체를 추출해 followingsList에 저장
        for (Follow follow : userFollowings)
            followingsList.add(follow.getFollowing());


        return followingsList;
    }

    // userId회원이 followUserId회원을 팔로우
    public void addFollow(int userId, int followUserId) {
        // 팔로우를 진행할 회원 정보 조회
        User follower = userService.getUser(userId);
        User following = userService.getUser(followUserId);

        // 이미 팔로우 중인지 확인
        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowing(follower, following);
        if (existingFollow.isPresent()) {
            throw new AlreadyFollowingException();
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
    }

    // userId회원이 followUserId회원을 언팔로우
    public void deleteFollow(int userId, int followUserId) {
        // 팔로우를 취소할 회원 정보 조회
        User follower = userService.getUser(userId);
        // 팔로우를 취소 당할 회원 정보 조회
        User following = userService.getUser(followUserId);

        // Follow 정보 찾기
        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowing(follower, following);
        if (existingFollow.isEmpty())
            // 팔로우 중이 아닌 경우 처리
            throw new FollowNotFoundException();

        // Follow 객체 삭제
        followRepository.delete(existingFollow.get());

        // 양방향 관계 삭제
        follower.getFollowings().remove(existingFollow.get());
        following.getFollowers().remove(existingFollow.get());

        // User 객체 저장
        userRepository.save(follower);
        userRepository.save(following);
    }
}
