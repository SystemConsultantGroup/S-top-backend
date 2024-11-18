package com.scg.stop.project.domain;

import com.scg.stop.global.exception.BadRequestException;

import java.util.HashMap;
import java.util.Map;

import static com.scg.stop.global.exception.ExceptionCode.INVALID_AWARD_STATUS_KOREAN_NAME;

public enum AwardStatus {

    NONE("없음"),
    FIRST("대상"),
    SECOND("최우수상"),
    THIRD("우수상"),
    FOURTH("장려상"),
    FIFTH("인기상");

    private static final Map<String, AwardStatus> KOREAN_NAME_MAP = new HashMap<>();
    private final String koreanName;

    static {
        for (AwardStatus awardStatus : AwardStatus.values()) {
            KOREAN_NAME_MAP.put(awardStatus.koreanName, awardStatus);
        }
    }

    AwardStatus(String koreanName) {
        this.koreanName = koreanName;
    }

    public static AwardStatus fromKoreanName(String koreanName) {
        if (!KOREAN_NAME_MAP.containsKey(koreanName)) {
            throw new BadRequestException(INVALID_AWARD_STATUS_KOREAN_NAME, String.format("수상 내역의 한글 이름이 올바르지 않습니다 : %s", koreanName));
        }
        return KOREAN_NAME_MAP.get(koreanName);
    }
}
