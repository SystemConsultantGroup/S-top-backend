package com.scg.stop.aihub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiHubModelRequest {

    private String title; // 제목 : text
    private List<String> learningModels;  // 학습 모델 : multiselect
    private List<String> topics;  // 주제 분류 : multiselect
    private List<Integer> developmentYears;  // 개발 년도 : multiselect
    private String professor; // 담당 교수 : text
    private List<String> participants; // 참여 학생 : text (comma separated)
}

