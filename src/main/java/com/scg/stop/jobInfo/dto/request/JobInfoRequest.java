package com.scg.stop.jobInfo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// TODO: Define what fields to query
public class JobInfoRequest {

    private String company; // 기업명 : text
    private List<String> jobTypes; // 고용 형태 : multiselect
    private String region; // 근무 지역 : text
    private String position; // 채용 포지션 : text
    private String hiringTime; // 채용 시점 : text
    private List<String> state; // 채용 상태 : multiselect

}
