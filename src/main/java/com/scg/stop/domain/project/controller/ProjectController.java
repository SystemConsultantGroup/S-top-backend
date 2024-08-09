package com.scg.stop.domain.project.controller;


import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.domain.project.domain.ProjectCategory;
import com.scg.stop.domain.project.dto.request.ProjectRequest;
import com.scg.stop.domain.project.dto.response.ProjectDetailResponse;
import com.scg.stop.domain.project.dto.response.ProjectResponse;
import com.scg.stop.domain.project.service.ProjectService;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
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
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
  
    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getProjects(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "category", required = false) ProjectCategory category,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ){
        Page<ProjectResponse> pageProjectResponse = projectService.getProjects(title, year, category, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageProjectResponse);
    }
  
    @PostMapping
    public ResponseEntity<ProjectDetailResponse> createProject(
            @RequestBody @Valid ProjectRequest projectRequest
    ) {
        ProjectDetailResponse projectDetailResponse = projectService.createProject(projectRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectDetailResponse);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponse> getProject(
            @PathVariable("projectId") Long projectId
    ) {
        ProjectDetailResponse projectDetailResponse = projectService.getProject(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(projectDetailResponse);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponse> updateProject(
            @PathVariable("projectId") Long projectId,
            @RequestBody @Valid ProjectRequest projectRequest
    ) {
        ProjectDetailResponse projectDetailResponse = projectService.updateProject(projectId, projectRequest);
        return ResponseEntity.status(HttpStatus.OK).body(projectDetailResponse);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable("projectId") Long projectId
    ) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/favorite")
    public ResponseEntity<Void> createProjectFavorite(
            @PathVariable("projectId") Long projectId,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ){
        projectService.createProjectFavorite(projectId, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{projectId}/favorite")
    public ResponseEntity<Void> deleteProjectFavorite(
            @PathVariable("projectId") Long projectId,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ){
        projectService.deleteProjectFavorite(projectId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/favorite")
    public ResponseEntity<Void> createProjectLike(
            @PathVariable("projectId") Long projectId,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ){
        projectService.createProjectLike(projectId, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{projectId}/favorite")
    public ResponseEntity<Void> deleteProjectLike(
            @PathVariable("projectId") Long projectId,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ){
        projectService.deleteProjectLike(projectId, user.getId());
        return ResponseEntity.noContent().build();
    }
}