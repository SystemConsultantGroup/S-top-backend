package com.scg.stop.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    INVALID_REQUEST(1000, "요청 형식이 올바르지 않습니다."),

    // event
    DUPLICATED_YEAR(1001, "해당 연도의 행사 기간이 이미 존재합니다."),

    // file
    NOT_FOUND_FILE_ID(2001, "요청한 ID에 해당하는 파일이 존재하지 않습니다.");

    private final int code;
    private final String message;
}