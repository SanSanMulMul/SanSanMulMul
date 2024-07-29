package com.sansantek.sansanmulmul.exception.follow;

public class FollowNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FollowNotFoundException() {
        super("팔로우 관계가 존재하지 않습니다.");
    }
}