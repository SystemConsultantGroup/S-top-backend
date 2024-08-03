package com.scg.stop.user.controller;

import com.scg.stop.user.dto.response.ApplicationDetailResponse;
import com.scg.stop.user.dto.response.ApplicationListResponse;
import com.scg.stop.user.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
            // TODO @AuthUser(accessType = {AccessType.ADMIN}) user
    ) {
        Page<ApplicationListResponse> applications = applicationService.getApplications(pageable);
        return ResponseEntity.ok().body(applications);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationDetailResponse> getApplication(
            @PathVariable("applicationId") Long applicationId
            // TODO @AuthUser(accessType = {AccessType.ADMIN}) user
    ) {
        ApplicationDetailResponse application = applicationService.getApplication(applicationId);
        return ResponseEntity.ok().body(application);
    }

    @PatchMapping("/{applicationId}")
    public ResponseEntity<ApplicationDetailResponse> updateApplication(
            @PathVariable("applicationId") Long applicationId
            // TODO @AuthUser(accessType = {AccessType.ADMIN}) user
    ) {
        ApplicationDetailResponse application = applicationService.updateApplication(applicationId);
        return ResponseEntity.ok().body(application);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> rejectApplication(
            @PathVariable("applicationId") Long applicationId
            // TODO @AuthUser(accessType = {AccessType.ADMIN}) user
    ) {
        applicationService.rejectApplication(applicationId);
        return ResponseEntity.noContent().build();
    }
}
