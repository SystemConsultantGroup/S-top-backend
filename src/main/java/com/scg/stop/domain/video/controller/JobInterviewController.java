package com.scg.stop.domain.video.controller;

import com.scg.stop.domain.video.domain.JobInterviewCategory;
import com.scg.stop.domain.video.dto.request.JobInterviewRequest;
import com.scg.stop.domain.video.dto.response.JobInterviewResponse;
import com.scg.stop.domain.video.service.JobInterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobInterviews")
public class JobInterviewController {
    private final JobInterviewService jobInterviewService;

    // only admin
    @PostMapping
    public ResponseEntity<JobInterviewResponse> createJobInterview(@RequestBody @Valid JobInterviewRequest jobInterviewDTO) {
        JobInterviewResponse interview = jobInterviewService.createJobInterview(jobInterviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(interview);
    }

    @GetMapping
    public ResponseEntity<Page<JobInterviewResponse>> getJobInterviews(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "category", required = false) JobInterviewCategory category,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<JobInterviewResponse> interviews = jobInterviewService.getJobInterviews(year, category, title, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(interviews);
    }

    @GetMapping("/{jobInterviewId}")
    public ResponseEntity<JobInterviewResponse> getJobInterview(@PathVariable("jobInterviewId") Long jobInterviewId) {
        return ResponseEntity.status(HttpStatus.OK).body(jobInterviewService.getJobInterview(jobInterviewId));
    }

    @PutMapping("/{jobInterviewId}")
    public ResponseEntity<JobInterviewResponse> updateJobInterview(
            @PathVariable("jobInterviewId")Long jobInterviewId,
            @RequestBody @Valid JobInterviewRequest jobInterviewDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(jobInterviewService.updateJobInterview(jobInterviewId, jobInterviewDTO));
    }

    @DeleteMapping("/{jobInterviewId}")
    public ResponseEntity<Void> deleteJobInterview(@PathVariable("jobInterviewId") Long jobInterviewId) {
        jobInterviewService.deleteJobInterviewById(jobInterviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
