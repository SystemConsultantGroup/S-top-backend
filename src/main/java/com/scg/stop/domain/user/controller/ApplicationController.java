package com.scg.stop.domain.user.controller;

import com.scg.stop.domain.user.dto.response.ApplicationListResponse;
import com.scg.stop.domain.user.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<Page<ApplicationListResponse>> getApplications(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<ApplicationListResponse> applications = applicationService.getApplications(pageable);
        return ResponseEntity.ok().body(applications);
    }

//    @GetMapping("/{applicationId}")
//    public void getApplication(@PathVariable String applicationId) {}
//
//    @PatchMapping("/{applicationId}")
//    public void updateApplication(@PathVariable String applicationId) {}
}
