package com.scg.stop.project.domain;

import com.scg.stop.global.exception.BadRequestException;

import java.util.HashMap;
import java.util.Map;

import static com.scg.stop.global.exception.ExceptionCode.INVALID_PROJECT_CATEGORY_KOREAN_NAME;

public enum ProjectCategory {

    COMPUTER_VISION("컴퓨터비전"),
    SYSTEM_NETWORK("시스템/네트워크"),
    WEB_APPLICATION("웹/어플리케이션"),
    SECURITY_SOFTWARE_ENGINEERING("보안/SW공학"),
    NATURAL_LANGUAGE_PROCESSING("자연어처리"),
    BIG_DATA_ANALYSIS("빅데이터분석"),
    AI_MACHINE_LEARNING("AI/머신러닝"),
    INTERACTION_AUGMENTED_REALITY("인터렉션/증강현실");

    private static final Map<String, ProjectCategory> KOREAN_NAME_MAP = new HashMap<>();
    private final String koreanName;

    static {
        for (ProjectCategory projectCategory : ProjectCategory.values()) {
            KOREAN_NAME_MAP.put(projectCategory.koreanName, projectCategory);
        }
    }

    ProjectCategory(String koreanName) {
        this.koreanName = koreanName;
    }

    public static ProjectCategory fromKoreanName(String koreanName) {
        if (!KOREAN_NAME_MAP.containsKey(koreanName)) {
            throw new BadRequestException(INVALID_PROJECT_CATEGORY_KOREAN_NAME, String.format("프로젝트 분야의 한글 이름이 올바르지 않습니다 : %s", koreanName));
        }
        return KOREAN_NAME_MAP.get(koreanName);
    }
}
