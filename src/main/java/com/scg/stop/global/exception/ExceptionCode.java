package com.scg.stop.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    INVALID_REQUEST(1000, "요청 형식이 올바르지 않습니다."),

    NOTICE_NOT_FOUND(2000, "요청한 ID에 해당하는 공지사항이 존재하지 않습니다."),

    FILE_NOT_FOUND(2001, "요청한 ID에 해당하는 파일이 존재하지 않습니다.");


    private final int code;
    private final String message;
}