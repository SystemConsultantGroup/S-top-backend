package com.scg.stop.domain.project.controller;

import com.scg.stop.domain.project.dto.request.ProjectRequest;
import com.scg.stop.domain.project.dto.response.ProjectDetailResponse;
import com.scg.stop.domain.project.service.ProjectService;
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

    @PostMapping
    public ResponseEntity<ProjectDetailResponse> createProject(
            @RequestBody @Valid ProjectRequest projectRequest
    ) {
        ProjectDetailResponse projectDetailResponse = projectService.createProject(projectRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectDetailResponse);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponse> getProject(
            @PathVariable Long projectId
    ) {
        ProjectDetailResponse projectDetailResponse = projectService.getProject(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(projectDetailResponse);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponse> updateProject(
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectRequest projectRequest
    ) {
        ProjectDetailResponse projectDetailResponse = projectService.updateProject(projectId, projectRequest);
        return ResponseEntity.status(HttpStatus.OK).body(projectDetailResponse);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long projectId
    ) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}