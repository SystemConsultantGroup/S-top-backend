package com.scg.stop.project.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.project.dto.request.InquiryRequest;
import com.scg.stop.project.dto.response.InquiryDetailResponse;
import com.scg.stop.project.service.ProjectService;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("{projectId}/inquiry")
    public InquiryDetailResponse createProjectInquiry(
            @PathVariable Long projectId,
            @AuthUser(accessType = AccessType.COMPANY) User user,
            @RequestBody @Valid InquiryRequest inquiryRequest) {
        return projectService.createProjectInquiry(projectId, user, inquiryRequest);

    }

}