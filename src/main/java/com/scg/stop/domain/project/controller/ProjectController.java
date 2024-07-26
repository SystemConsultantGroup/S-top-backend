package com.scg.stop.domain.project.controller;

import com.scg.stop.domain.project.domain.ProjectCategory;
import com.scg.stop.domain.project.dto.response.ProjectResponse;
import com.scg.stop.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getProjects(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "category", required = false) ProjectCategory category,
            @PageableDefault(page = 0, size = 10) Pageable pageable){
        Page<ProjectResponse> projects = projectService.getProjects(title, year, category, pageable);
        return ResponseEntity.ok(projects);
    }
}
