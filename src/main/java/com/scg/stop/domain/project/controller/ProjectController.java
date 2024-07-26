package com.scg.stop.domain.project.controller;

import com.scg.stop.domain.project.dto.request.ProjectRequest;
import com.scg.stop.domain.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Void> createProject(
            @RequestBody @Valid ProjectRequest projectRequest
    ) {
        projectService.createProject(projectRequest);
        return ResponseEntity.ok().build();
    }


}