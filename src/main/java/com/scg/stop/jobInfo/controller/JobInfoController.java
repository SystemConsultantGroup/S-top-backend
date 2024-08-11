package com.scg.stop.jobInfo.controller;

import com.scg.stop.jobInfo.dto.request.JobInfoRequest;
import com.scg.stop.jobInfo.dto.response.JobInfoResponse;
import com.scg.stop.jobInfo.service.JobInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobInfo")
@RequiredArgsConstructor
public class JobInfoController {

    private final JobInfoService jobInfoService;

    // Get the paginated list of jobInfos
    @PostMapping
    public ResponseEntity<Page<JobInfoResponse>> getJobInfos(
            @RequestBody JobInfoRequest request,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<JobInfoResponse> jobInfos = jobInfoService.getJobInfo(request, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(jobInfos);
    }


}
