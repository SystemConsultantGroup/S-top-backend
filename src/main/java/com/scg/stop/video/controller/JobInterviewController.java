package com.scg.stop.video.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.video.domain.JobInterviewCategory;
import com.scg.stop.video.dto.request.JobInterviewRequest;
import com.scg.stop.video.dto.response.JobInterviewResponse;
import com.scg.stop.video.dto.response.JobInterviewUserResponse;
import com.scg.stop.video.service.FavoriteVideoService;
import com.scg.stop.video.service.JobInterviewService;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobInterviews")
public class JobInterviewController {
    private final JobInterviewService jobInterviewService;
    private final FavoriteVideoService favoriteVideoService;

    @PostMapping
    public ResponseEntity<JobInterviewResponse> createJobInterview(
            @RequestBody @Valid JobInterviewRequest jobInterviewDTO,
            @AuthUser(accessType = {AccessType.ADMIN}) User user
    ) {
        JobInterviewResponse interview = jobInterviewService.createJobInterview(jobInterviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(interview);
    }

    @GetMapping
    public ResponseEntity<Page<JobInterviewUserResponse>> getJobInterviews(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "category", required = false) JobInterviewCategory category,
            @AuthUser(accessType = {AccessType.OPTIONAL}) User user,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(jobInterviewService.getJobInterviews(user, year, category, title, pageable));
    }

    @GetMapping("/{jobInterviewId}")
    public ResponseEntity<JobInterviewUserResponse> getJobInterview(
            @PathVariable("jobInterviewId") Long jobInterviewId,
            @AuthUser(accessType = {AccessType.OPTIONAL}) User user
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(jobInterviewService.getJobInterview(jobInterviewId, user));
    }

    @PutMapping("/{jobInterviewId}")
    public ResponseEntity<JobInterviewResponse> updateJobInterview(
            @PathVariable("jobInterviewId")Long jobInterviewId,
            @RequestBody @Valid JobInterviewRequest jobInterviewDTO,
            @AuthUser(accessType = {AccessType.ADMIN}) User user
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(jobInterviewService.updateJobInterview(jobInterviewId, jobInterviewDTO));
    }

    @DeleteMapping("/{jobInterviewId}")
    public ResponseEntity<Void> deleteJobInterview(
            @PathVariable("jobInterviewId") Long jobInterviewId,
            @AuthUser(accessType = {AccessType.ADMIN}) User user
    ) {
        jobInterviewService.deleteJobInterviewById(jobInterviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{jobInterviewId}/favorite")
    public ResponseEntity<Void> createJobInterviewFavorite(
            @PathVariable("jobInterviewId") Long jobInterviewId,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ) {
        favoriteVideoService.createJobInterviewFavorite(jobInterviewId, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{jobInterviewId}/favorite")
    public ResponseEntity<Void> deleteJobInterviewFavorite(
            @PathVariable("jobInterviewId") Long jobInterviewId,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ) {
        favoriteVideoService.deleteJobInterviewFavorite(jobInterviewId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
