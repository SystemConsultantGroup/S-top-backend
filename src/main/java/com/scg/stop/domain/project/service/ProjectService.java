package com.scg.stop.domain.project.service;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.file.repository.FileRepository;
import com.scg.stop.domain.project.domain.Project;
import com.scg.stop.domain.project.dto.request.ProjectRequest;
import com.scg.stop.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final FileRepository fileRepository;
    @Transactional
    public void createProject(ProjectRequest projectRequest) {
        File thumbnail = fileRepository.findById(projectRequest.getThumbnailId())
                .orElseThrow(() -> new IllegalArgumentException("썸네일을 찾을 수 없습니다"));
        File poster = fileRepository.findById(projectRequest.getPosterId())
                .orElseThrow(() -> new IllegalArgumentException("포스터를 찾을 수 없습니다"));

        // ToDo: BadRequestException dev 브랜치에 있을듯..
        Project project = projectRequest.toEntity(thumbnail, poster);

        projectRepository.save(project);
    }
}