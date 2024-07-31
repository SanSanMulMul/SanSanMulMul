package com.sansantek.sansanmulmul.user.repository.follow;

import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.follow.Follow;
import com.sansantek.sansanmulmul.user.domain.follow.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    List<Follow> findByFollower_UserId(int followerUserId);
    List<Follow> findByFollowing_UserId(int followingUserId);
//    Follow findByFollowerAndFollowing(User follower, User following);
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
}
