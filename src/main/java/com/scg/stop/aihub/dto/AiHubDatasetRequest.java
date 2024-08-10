package com.scg.stop.aihub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiHubDatasetRequest {

    private String title; // 제목
    private List<String> dataTypes; // 데이터 유형 (List of data types)
    private List<String> topics;  // 주제 분류
    private List<Integer> developmentYears;  // 구축년도
    private String professor; // 담당 교수
    private List<String> participants; // 참여 학생
}

