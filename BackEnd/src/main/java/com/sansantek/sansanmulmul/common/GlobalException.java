package com.sansantek.sansanmulmul.common;


import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final ErrorCode errorCode;

    public GlobalException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}