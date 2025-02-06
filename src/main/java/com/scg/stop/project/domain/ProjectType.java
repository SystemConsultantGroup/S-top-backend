package com.scg.stop.project.domain;

import com.scg.stop.global.exception.BadRequestException;

import java.util.HashMap;
import java.util.Map;

import static com.scg.stop.global.exception.ExceptionCode.INVALID_PROJECT_TYPE_KOREAN_NAME;

public enum ProjectType {

    RESEARCH_AND_BUSINESS_FOUNDATION("산학"),
    LAB("연구실"),
    STARTUP("창업/SPARK"),
    CLUB("동아리");

    private static final Map<String, ProjectType> KOREAN_NAME_MAP = new HashMap<>();
    private final String koreanName;

    static {
        for (ProjectType projectType : ProjectType.values()) {
            KOREAN_NAME_MAP.put(projectType.koreanName, projectType);
        }
    }

    ProjectType(String koreanName) {
        this.koreanName = koreanName;
    }

    public static ProjectType fromKoreanName(String koreanName) {
        if (!KOREAN_NAME_MAP.containsKey(koreanName)) {
            throw new BadRequestException(INVALID_PROJECT_TYPE_KOREAN_NAME, String.format("프로젝트 종류의 한글 이름이 올바르지 않습니다 : %s", koreanName));
        }
        return KOREAN_NAME_MAP.get(koreanName);
    }
}
