package com.scg.stop.domain.project.service;

import com.scg.stop.domain.project.domain.*;
import com.scg.stop.domain.project.dto.response.ProjectResponse;
import com.scg.stop.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public Page<ProjectResponse> getProjects(String title, Integer year, ProjectCategory category, Pageable pageable){

        Page<Project> projects = projectRepository.findProjects(title, year, category, pageable);

        Page<ProjectResponse> projectResponses = projects.map(project -> {
            List<String> studentNames = project.getMembers().stream()
                    .filter(member -> member.getRole() == Role.STUDENT)
                    .map(Member::getName)
                    .collect(Collectors.toList());
            List<String> professorNames = project.getMembers().stream()
                    .filter(member -> member.getRole() == Role.PROFESSOR)
                    .map(Member::getName)
                    .collect(Collectors.toList());
            return ProjectResponse.of(studentNames, professorNames, project);
        });

        return projectResponses;
    }
}
