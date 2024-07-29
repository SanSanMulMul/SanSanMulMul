package com.sansantek.sansanmulmul.exception;

public class AlreadyFollowingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AlreadyFollowingException() {
        super("이미 팔로우 관계가 성립되어 있습니다.");
    }
}