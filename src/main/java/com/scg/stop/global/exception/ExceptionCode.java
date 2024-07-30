package com.scg.stop.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    INVALID_REQUEST(1000, "요청 형식이 올바르지 않습니다."),

    // event domain
    DUPLICATED_YEAR(1001, "해당 연도의 행사 기간이 이미 존재합니다."),

    // user domain
    NOT_FOUND_APPLICATION_ID(4010, "ID에 해당하는 가입 신청 정보가 존재하지 않습니다."),
    ALREADY_VERIFIED_USER(4011, "이미 인증 된 회원입니다.");

    private final int code;
    private final String message;
}