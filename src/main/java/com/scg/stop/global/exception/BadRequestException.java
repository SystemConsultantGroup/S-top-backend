package com.scg.stop.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BadRequestException extends RuntimeException {

    private final int status;
    private final String message;

    public BadRequestException(ExceptionCode exceptionCode) {
        this.status = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}