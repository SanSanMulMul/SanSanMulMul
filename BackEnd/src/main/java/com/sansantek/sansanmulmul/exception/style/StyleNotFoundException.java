package com.sansantek.sansanmulmul.exception.style;

public class StyleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StyleNotFoundException() {
        super("해당 회원의 해당 스타일이 존재하지 않습니다.");
    }
}
