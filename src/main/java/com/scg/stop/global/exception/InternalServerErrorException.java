package com.scg.stop.global.exception;

import lombok.Getter;

@Getter
public class InternalServerErrorException extends RuntimeException {

    private final int code;
    private final String message;

    public InternalServerErrorException(final ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
