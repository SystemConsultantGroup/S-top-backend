package com.scg.stop.aihub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiHubModelRequest {

    private String title; // 제목
    private String professor; // 담당 교수
    private List<String> participants; // 참여 학생 (List of student names)
    private List<String> learningModels;  // 학습 모델
    private List<String> topics;  // 주제 분류
    private List<String> developmentYears;  // 개발 년도
}

