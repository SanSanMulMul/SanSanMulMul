package com.sansantek.sansanmulmul.exception;

public class InvalidTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidTokenException() {
        super("토큰 유효성 검사에 실패했습니다.\n다시 로그인을 하세요.");
    }
}
