package com.scg.stop.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    INVALID_REQUEST(1000, "요청 형식이 올바르지 않습니다."),

    DUPLICATED_YEAR(1001, "해당 연도의 행사 기간이 이미 존재합니다."),

    ID_NOT_FOUND(8200,"해당 ID에 해당하는 잡페어 인터뷰가 없습니다.");

    private final int code;
    private final String message;
}