package com.scg.stop.global.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {

    private final int code;
    private final String message;
    public UnauthorizedException(ExceptionCode exceptionCode) {
        code = exceptionCode.getCode();
        message = exceptionCode.getMessage();
    }
}
