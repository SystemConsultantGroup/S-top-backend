package com.scg.stop.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    INVALID_REQUEST(1000, "요청 형식이 올바르지 않습니다."),

    // event
    DUPLICATED_YEAR(1001, "올해의 이벤트 기간이 이미 존재합니다."),
    NOT_FOUND_EVENT_PERIOD(1002, "올해의 이벤트 기간이 존재하지 않습니다."),
    INVALID_EVENT_PERIOD(1003, "이벤트 시작 일시 혹은 종료 일시가 올해를 벗어났습니다."),

    UNABLE_TO_GET_USER_INFO(2001, "소셜 로그인 공급자로부터 유저 정보를 받아올 수 없습니다."),
    UNABLE_TO_GET_ACCESS_TOKEN(2002, "소셜 로그인 공급자로부터 인증 토큰을 받아올 수 없습니다."),

    UNAUTHORIZED_ACCESS(3000, "접근할 수 없는 리소스입니다."),
    INVALID_REFRESH_TOKEN(3001,"유효하지 않은 Refresh Token 입니다."),
    FAILED_TO_VALIDATE_TOKEN(3002,"토큰 검증에 실패했습니다."),
    INVALID_ACCESS_TOKEN(3003,"유효하지 않은 Access Token 입니다."),

    NOT_FOUND_USER_ID(4000, "유저 id 를 찾을 수 없습니다."),
    REGISTER_NOT_FINISHED(4001, "회원가입이 필요합니다."),
    NOT_AUTHORIZED(4002, "유저 권한이 존재하지 않습니다."),
    NOT_FOUND_DEPARTMENT(4003, "학과가 존재하지 않습니다."),
    INVALID_STUDENTINFO(4004, "학과/학번 정보가 존재하지 않습니다."),

    NOTICE_NOT_FOUND(10000, "요청한 ID에 해당하는 공지사항이 존재하지 않습니다."),
    EVENT_NOTICE_NOT_FOUND(11000, "요청한 ID에 해당하는 이벤트가 존재하지 않습니다."),
    FILE_NOT_FOUND(12000, "요청한 ID에 해당하는 파일이 존재하지 않습니다."),

    ID_NOT_FOUND(8200,"해당 ID에 해당하는 잡페어 인터뷰가 없습니다."),
    TALK_ID_NOT_FOUND(8400, "해당 ID에 해당하는 대담 영상이 없습니다."),
    NO_QUIZ(8401, "퀴즈 데이터가 존재하지 않습니다.");


    private final int code;
    private final String message;
}