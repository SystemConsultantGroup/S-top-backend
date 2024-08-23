package com.scg.stop.project.controller;


import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.project.domain.ProjectCategory;
import com.scg.stop.project.dto.request.CommentRequest;
import com.scg.stop.project.dto.request.ProjectRequest;
import com.scg.stop.project.dto.response.CommentResponse;
import com.scg.stop.project.dto.response.ProjectDetailResponse;
import com.scg.stop.project.dto.response.ProjectResponse;
import com.scg.stop.project.service.ProjectService;
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
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @AuthUser(accessType = {AccessType.ALL}) User user // ToDo: 이게 로그인이 안된 상태이면, user가 null로 처리가 되어야함...
    ){
        Page<ProjectResponse> pageProjectResponse = projectService.getProjects(title, year, category, pageable, user);
        return ResponseEntity.status(HttpStatus.OK).body(pageProjectResponse);
    }
  
    @PostMapping
    public ResponseEntity<ProjectDetailResponse> createProject(
            @RequestBody @Valid ProjectRequest projectRequest,
            @AuthUser(accessType = {AccessType.ADMIN}) User user
    ) {
        ProjectDetailResponse projectDetailResponse = projectService.createProject(projectRequest, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectDetailResponse);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponse> getProject(
            @PathVariable("projectId") Long projectId,
            @AuthUser(accessType = {AccessType.ALL}) User user // ToDo: 이게 로그인이 안된 상태이면, user가 null로 처리가 되어야함...
    ) {
        ProjectDetailResponse projectDetailResponse = projectService.getProject(projectId, user);
        return ResponseEntity.status(HttpStatus.OK).body(projectDetailResponse);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponse> updateProject(
            @PathVariable("projectId") Long projectId,
            @RequestBody @Valid ProjectRequest projectRequest,
            @AuthUser(accessType = {AccessType.ADMIN}) User user
    ) {
        ProjectDetailResponse projectDetailResponse = projectService.updateProject(projectId, projectRequest, user);
        return ResponseEntity.status(HttpStatus.OK).body(projectDetailResponse);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable("projectId") Long projectId,
            @AuthUser(accessType = {AccessType.ADMIN}) User user
    ) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/favorite")
    public ResponseEntity<Void> createProjectFavorite(
            @PathVariable("projectId") Long projectId,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ){
        projectService.createProjectFavorite(projectId, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{projectId}/favorite")
    public ResponseEntity<Void> deleteProjectFavorite(
            @PathVariable("projectId") Long projectId,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ){
        projectService.deleteProjectFavorite(projectId, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/like")
    public ResponseEntity<Void> createProjectLike(
            @PathVariable("projectId") Long projectId,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ){
        projectService.createProjectLike(projectId, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{projectId}/like")
    public ResponseEntity<Void> deleteProjectLike(
            @PathVariable("projectId") Long projectId,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ){
        projectService.deleteProjectLike(projectId, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/comment")
    public ResponseEntity<CommentResponse> createProjectComment(
            @PathVariable("projectId") Long projectId,
            @RequestBody @Valid CommentRequest commentRequest,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ){
        CommentResponse commentResponse = projectService.createProjectComment(projectId, user, commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponse);
    }

    @DeleteMapping("/{projectId}/comment/{commentId}")
    public ResponseEntity<Void> deleteProjectComment(
            @PathVariable("projectId") Long projectId,
            @PathVariable("commentId") Long commentId,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ){
        projectService.deleteProjectComment(projectId, commentId, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/award")
    public ResponseEntity<Page<ProjectResponse>> getAwardProjects(
            @RequestParam(value = "year", required = true) Integer year,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ){
        Page<ProjectResponse> pageProjectResponse = projectService.getAwardProjects(year, pageable, user);
        return ResponseEntity.status(HttpStatus.OK).body(pageProjectResponse);
    }
}
