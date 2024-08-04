package com.sansantek.sansanmulmul.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Internal Server Error
    INTERNAL_SERVER_ERROR("C001", HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),

    // User Error
    USER_REGISTER_FAILED("U001", HttpStatus.BAD_REQUEST, "사용자 등록에 실패했습니다."),
    USER_NOT_FOUND("U002", HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH("U003", HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    USER_NOT_AUTHORIZED("U004", HttpStatus.FORBIDDEN, "해당 작업을 수행할 권한이 없습니다."),

    // Unauthorized
    AUTHENTICATION_FAILED("A001", HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    NO_JWT_TOKEN("A002", HttpStatus.UNAUTHORIZED, "JWT 토큰이 없습니다."),
    INVALID_JWT_TOKEN("A003", HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    ACCESS_TOKEN_EXPIRED("A004", HttpStatus.UNAUTHORIZED, "Access Token이 만료되었습니다."),
    REFRESH_TOKEN_EXPIRED("A005", HttpStatus.UNAUTHORIZED, "Refresh Token이 만료되었습니다."),
    REFRESH_TOKEN_BLACKLISTED("A006", HttpStatus.UNAUTHORIZED, "블랙리스트에 등록된 Refresh Token입니다."),

    // Group-User Error
    GROUP_REQUEST_FAILED("G001", HttpStatus.BAD_REQUEST, "그룹 가입 요청에 실패했습니다."),
    GROUP_ACCEPT_FAILED("G002", HttpStatus.BAD_REQUEST, "그룹 가입 수락에 실패했습니다."),
    GROUP_DENY_FAILED("G003", HttpStatus.BAD_REQUEST, "그룹 가입 거절에 실패했습니다."),
    GROUP_CONFIRM_FAILED("G004", HttpStatus.BAD_REQUEST, "구매 확정에 실패했습니다."),
    GROUP_NOT_FOUND("G005", HttpStatus.NOT_FOUND, "판매 정보를 찾을 수 없습니다."),
    GROUP_RESERVED_FAILED("G006", HttpStatus.BAD_REQUEST, "예약된 정보를 가져올 수 없습니다."),

    //s3
    FILE_NOT_FOUND("S3001", HttpStatus.NOT_FOUND, "파일이 존재하지 않습니다."),
    AWS_SERVER_ERROR("S3002", HttpStatus.INTERNAL_SERVER_ERROR, "AWS 서버 에러입니다."),

    //image
    FILE_UPLOAD_ERROR("I001",HttpStatus.INTERNAL_SERVER_ERROR, "첨부한 파일이 S3에 업로드 되지 않았습니다."),
    IMAGE_NOT_FOUND("I002", HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),

    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}