package com.sansantek.sansanmulmul.exception.auth;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super("사용자 조회에 실패했습니다.\n 아이디를 다시 확인해주세요.");
    }
}