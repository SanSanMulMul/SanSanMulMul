package com.sansantek.sansanmulmul.exception.style;

public class AlreadyStyleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AlreadyStyleException() {
        super("이미 스타일이 추가되어 있습니다.");
    }
}
