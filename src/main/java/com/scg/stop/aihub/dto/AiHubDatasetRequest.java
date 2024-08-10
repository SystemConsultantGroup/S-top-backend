package com.scg.stop.aihub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiHubDatasetRequest {

    private String title; // 제목 : text
    private List<String> dataTypes; // 데이터 유형 : multiselect
    private List<String> topics;  // 주제 분류 : multiselect
    private List<Integer> developmentYears;  // 구축 년도 : multiselect
    private String professor; // 담당 교수 : text
    private List<String> participants; // 참여 학생 : text (comma separated)
}

