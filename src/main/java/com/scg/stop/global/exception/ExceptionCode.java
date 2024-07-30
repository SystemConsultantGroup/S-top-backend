package com.scg.stop.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    INVALID_REQUEST(1000, "요청 형식이 올바르지 않습니다."),

    DUPLICATED_YEAR(1001, "해당 연도의 행사 기간이 이미 존재합니다."),

    NOT_FOUND_PROJECT(77000, "프로젝트를 찾을 수 없습니다."),
    NOT_FOUND_PROJECT_THUMBNAIL(77001, "프로젝트 썸네일을 찾을 수 없습니다"),
    NOT_FOUND_PROJECT_POSTER(77001, "프로젝트 포스터를 찾을 수 없습니다");

    private final int code;
    private final String message;
}