package com.scg.stop.global.exception;

import lombok.Getter;

@Getter
public class InvalidJwtException extends RuntimeException{
    private final int code;
    private final String message;

    public InvalidJwtException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
