package com.sansantek.sansanmulmul.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private static final String SUCCESS_STATUS = "success";

    private String status;
    private T data;
    private String message;

    public static <T> ApiResponse<T> createSuccess(T data, String message) {
        return new ApiResponse<>(SUCCESS_STATUS, data, message);
    }

    public static ApiResponse<?> createSuccessWithNoContent(String message) {
        return new ApiResponse<>(SUCCESS_STATUS, null, message);
    }

    // 예외 발생으로 API 호출 실패시 반환
    public static ApiResponse<?> createError(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getCode(), null, errorCode.getMessage());
    }

    public static ApiResponse<?> createError(String errorCode, String message) {
        return new ApiResponse<>(errorCode, null, message);
    }

    private ApiResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
}