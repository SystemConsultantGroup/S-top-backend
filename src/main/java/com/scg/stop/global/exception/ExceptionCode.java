package com.scg.stop.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    INVALID_REQUEST(1000, "요청 형식이 올바르지 않습니다."),

    // event domain
    DUPLICATED_YEAR(1001, "해당 연도의 행사 기간이 이미 존재합니다."),
    NOT_FOUND_EVENT_PERIOD(1002, "올해의 이벤트 기간이 존재하지 않습니다."),
    INVALID_EVENT_PERIOD(1003, "이벤트 시작 일시 혹은 종료 일시가 올해를 벗어났습니다."),

    // auth domain
    UNABLE_TO_GET_USER_INFO(2001, "소셜 로그인 공급자로부터 유저 정보를 받아올 수 없습니다."),
    UNABLE_TO_GET_ACCESS_TOKEN(2002, "소셜 로그인 공급자로부터 인증 토큰을 받아올 수 없습니다."),
    UNAUTHORIZED_ACCESS(3000, "접근할 수 없는 리소스입니다."),
    INVALID_REFRESH_TOKEN(3001,"유효하지 않은 Refresh Token 입니다."),
    FAILED_TO_VALIDATE_TOKEN(3002,"토큰 검증에 실패했습니다."),
    INVALID_ACCESS_TOKEN(3003,"유효하지 않은 Access Token 입니다."),

    // notion domain
    FAILED_TO_FETCH_NOTION_DATA(13000, "Notion 데이터를 가져오는데 실패했습니다."),


    // user domain
    NOT_FOUND_USER_ID(4000, "유저 id 를 찾을 수 없습니다."),
    REGISTER_NOT_FINISHED(4001, "회원가입이 필요합니다."),
    NOT_AUTHORIZED(4002, "유저 권한이 존재하지 않습니다."),
    NOT_FOUND_DEPARTMENT(4003, "학과가 존재하지 않습니다."),
    INVALID_STUDENTINFO(4004, "학과/학번 정보가 존재하지 않습니다."),
    INVALID_USERTYPE(4005, "회원 가입 이용이 불가능한 회원 유형입니다."),
    NOT_FOUND_APPLICATION_ID(4010, "ID에 해당하는 인증 신청 정보가 존재하지 않습니다."),
    ALREADY_VERIFIED_USER(4011, "이미 인증 된 회원입니다."),

    // project domain
    NOT_FOUND_PROJECT(77000, "프로젝트를 찾을 수 없습니다."),
    NOT_FOUND_PROJECT_THUMBNAIL(77001, "프로젝트 썸네일을 찾을 수 없습니다"),
    NOT_FOUND_PROJECT_POSTER(77002, "프로젝트 포스터를 찾을 수 없습니다"),
    INVALID_MEMBER(77003, "멤버 정보가 올바르지 않습니다."),
    INVALID_TECHSTACK(77004, "기술 스택 정보가 올바르지 않습니다."),
    ALREADY_FAVORITE_PROJECT(77005, "관심 표시한 프로젝트가 이미 존재합니다"),
    NOT_FOUND_FAVORITE_PROJECT(77007, "관심 표시한 프로젝트를 찾을 수 없습니다."),
    ALREADY_LIKE_PROJECT(77008, "이미 좋아요 한 프로젝트입니다."),
    NOT_FOUND_LIKE_PROJECT(77009, "좋아요 표시한 프로젝트가 존재하지 않습니다"),
    NOT_FOUND_COMMENT(77010, "댓글을 찾을 수 없습니다"),
    NOT_MATCH_USER(77011, "유저 정보가 일치하지 않습니다"),

    // file domain
    FAILED_TO_UPLOAD_FILE(5000, "파일 업로드를 실패했습니다."),
    FAILED_TO_GET_FILE(5001, "파일 가져오기를 실패했습니다."),
    FILE_NOT_FOUND(5002, "요청한 ID에 해당하는 파일이 존재하지 않습니다."),
    NOT_FOUND_FILE_ID(5002, "요청한 ID에 해당하는 파일이 존재하지 않습니다."),
    INVALID_FILE_PATH(5004, "파일을 찾을 수 없습니다."),

    // notice domain
    NOTICE_NOT_FOUND(10000, "요청한 ID에 해당하는 공지사항이 존재하지 않습니다."),
    EVENT_NOTICE_NOT_FOUND(11000, "요청한 ID에 해당하는 이벤트가 존재하지 않습니다."),

    // inquiry domain
    NOT_FOUND_INQUIRY(8000, "요청한 ID에 해당하는 문의가 존재하지 않습니다."),
    NOT_FOUND_INQUIRY_REPLY(8001, "요청한 ID에 해당하는 문의 답변이 존재하지 않습니다."),
    ALREADY_EXIST_INQUIRY_REPLY(8002, "이미 답변이 등록된 문의입니다."),
    UNAUTHORIZED_USER(8003, "해당 문의에 대한 권한이 없습니다."),

    // video domain
    ID_NOT_FOUND(8200,"해당 ID에 해당하는 잡페어 인터뷰가 없습니다."),
    TALK_ID_NOT_FOUND(8400, "해당 ID에 해당하는 대담 영상이 없습니다."),
    NO_QUIZ(8401, "퀴즈 데이터가 존재하지 않습니다."),
    NOT_FOUND_USER_QUIZ(8402, "퀴즈 제출 데이터가 존재하지 않습니다."),
    ALREADY_QUIZ_SUCCESS(8601, "이미 퀴즈의 정답을 모두 맞추었습니다."),
    ALREADY_FAVORITE(8801, "이미 관심 리스트에 추가되었습니다."),
    NOT_FAVORITE(8802, "이미 관심 리스트에 추가되어 있지 않습니다."),
    NOT_EVENT_PERIOD(8804, "퀴즈 이벤트 참여 기간이 아닙니다."),
    MISMATCH_CURRENT_YEAR(8805, "대담 영상과 현재 이벤트 참여 연도가 일치하지 않습니다."),
    TOO_MANY_TRY_QUIZ(8901, "퀴즈 최대 시도 횟수를 초과하였습니다."),

    // excel
    NOT_COMPATIBLE_EXCEL(71001, "엑셀 파일이 주어진 클래스와 호환되지 않습니다."),

    // gallery domain
    NOT_FOUND_GALLERY_ID(9001, "요청한 ID에 해당하는 갤러리가 존재하지 않습니다.");

    private final int code;
    private final String message;
}
