package com.scg.stop.domain.video.controller;

import com.scg.stop.domain.video.domain.Category;
import com.scg.stop.domain.video.dto.JobInterviewDto;
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
    public ResponseEntity<JobInterviewDto.Response> createJobInterview(@RequestBody @Valid JobInterviewDto.Request jobInterviewDTO) {
        JobInterviewDto.Response interview = jobInterviewService.createJobInterview(jobInterviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(interview);
    }

    @GetMapping
    public ResponseEntity<Page<JobInterviewDto.Response>> getJobInterviews(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "category", required = false) Category category,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<JobInterviewDto.Response> interviews = jobInterviewService.getJobInterviews(year, category, title, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(interviews);
    }

    @GetMapping("/{jobInterviewId}")
    public ResponseEntity<JobInterviewDto.Response> getJobInterview(@PathVariable Long jobInterviewId) {
        return ResponseEntity.status(HttpStatus.OK).body(jobInterviewService.getJobInterview(jobInterviewId));
    }

    @PutMapping("/{jobInterviewId}")
    public ResponseEntity<JobInterviewDto.Response> updateJobInterview(@PathVariable Long jobInterviewId, @RequestBody @Valid JobInterviewDto.Request jobInterviewDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(jobInterviewService.updateJobInterview(jobInterviewId, jobInterviewDTO));
    }

    @DeleteMapping("/{jobInterviewId}")
    public ResponseEntity<Void> deleteJobInterview(@PathVariable Long jobInterviewId) {
        jobInterviewService.deleteJobInterviewById(jobInterviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
