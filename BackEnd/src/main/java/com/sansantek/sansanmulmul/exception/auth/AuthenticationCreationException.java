package com.sansantek.sansanmulmul.exception.auth;

public class AuthenticationCreationException extends RuntimeException {
    public AuthenticationCreationException(String message) {
        super(message);
    }

    public AuthenticationCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}