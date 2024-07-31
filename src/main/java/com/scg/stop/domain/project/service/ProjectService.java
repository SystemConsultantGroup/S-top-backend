package com.scg.stop.domain.project.service;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.file.repository.FileRepository;
import com.scg.stop.domain.project.domain.Member;
import com.scg.stop.domain.project.domain.Project;
import com.scg.stop.domain.project.domain.Role;
import com.scg.stop.domain.project.dto.request.ProjectRequest;
import com.scg.stop.domain.project.dto.response.ProjectDetailResponse;
import com.scg.stop.domain.project.repository.ProjectRepository;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final FileRepository fileRepository;

    public ProjectDetailResponse createProject(ProjectRequest projectRequest) {
        File thumbnail = fileRepository.findById(projectRequest.getThumbnailId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_THUMBNAIL));
        File poster = fileRepository.findById(projectRequest.getPosterId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_POSTER));

        Project project = projectRequest.toEntity(null, thumbnail, poster);

        projectRepository.save(project);

        return getProject(project.getId());
    }

    public ProjectDetailResponse getProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        List<String> studentNames = project.getMembers().stream()
                .filter(member -> member.getRole() == Role.STUDENT)
                .map(Member::getName)
                .collect(Collectors.toList());
        List<String> professerNames = project.getMembers().stream()
                .filter(member -> member.getRole() == Role.PROFESSOR)
                .map(Member::getName)
                .collect(Collectors.toList());

        return ProjectDetailResponse.of(studentNames, professerNames, project);
    }

    public ProjectDetailResponse updateProject(Long projectId, ProjectRequest projectRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        File thumbnail = fileRepository.findById(projectRequest.getThumbnailId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_THUMBNAIL));
        File poster = fileRepository.findById(projectRequest.getPosterId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_POSTER));

        Project newProject = projectRequest.toEntity(projectId, thumbnail, poster);
        project.update(newProject);

        return getProject(projectId);
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        projectRepository.delete(project);
    }
}