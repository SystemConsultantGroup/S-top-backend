package com.scg.stop.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    UNABLE_TO_GET_USER_INFO(2001, "소셜 로그인 공급자로부터 유저 정보를 받아올 수 없습니다."),
    UNABLE_TO_GET_ACCESS_TOKEN(2002, "소셜 로그인 공급자로부터 인증 토큰을 받아올 수 없습니다."),

    UNAUTHORIZED_ACCESS(3000, "접근할 수 없는 리소스입니다."),
    INVALID_REFRESH_TOKEN(3001,"유효하지 않은 Refresh Token 입니다."),
    FAILED_TO_VALIDATE_TOKEN(3002,"토큰 검증에 실패했습니다."),
    INVALID_ACCESS_TOKEN(3003,"유효하지 않은 Access Token 입니다."),

    NOT_FOUND_USER_ID(4000, "유저 id 를 찾을 수 없습니다."),
    REGISTER_NOT_FINISHED(4001, "회원가입이 필요합니다.");

    private final int code;
    private final String message;
}
