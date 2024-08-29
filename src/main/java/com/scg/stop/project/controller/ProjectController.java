package com.scg.stop.project.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.project.dto.request.InquiryRequest;
import com.scg.stop.project.dto.response.InquiryDetailResponse;
import com.scg.stop.project.service.ProjectService;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    // 문의 생성
    @PostMapping("/{projectId}/inquiry")
    public ResponseEntity<InquiryDetailResponse> createProjectInquiry(
            @PathVariable Long projectId,
            @AuthUser(accessType = AccessType.COMPANY) User user,
            @RequestBody @Valid InquiryRequest inquiryRequest) {

        InquiryDetailResponse inquiryDetailResponse = projectService.createProjectInquiry(projectId, user, inquiryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(inquiryDetailResponse);

    }

}