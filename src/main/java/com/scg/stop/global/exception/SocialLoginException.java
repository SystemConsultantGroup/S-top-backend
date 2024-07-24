package com.scg.stop.global.exception;

import lombok.Getter;

@Getter
public class SocialLoginException extends RuntimeException{
    private final int code;
    private final String message;

    public SocialLoginException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
