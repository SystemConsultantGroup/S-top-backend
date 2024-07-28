package com.scg.stop.global.exception;

public class UnauthorizedException extends RuntimeException {

    private final int code = 401;
    private final String message;
    public UnauthorizedException(ExceptionCode exceptionCode) {
        message = exceptionCode.getMessage();
    }
}
