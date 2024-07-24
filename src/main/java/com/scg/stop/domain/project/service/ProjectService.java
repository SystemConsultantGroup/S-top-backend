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
        return projects.map(this::entityToProjectResponse);
    }

    private ProjectResponse entityToProjectResponse(Project project) {
        List<String> studentsName = project.getMembers().stream()
                .filter(member -> member.getRole() == Role.STUDENT)
                .map(Member::getName)
                .collect(Collectors.toList());
        String professorName = project.getMembers().stream()
                .filter(member -> member.getRole() == Role.PROFESSOR)
                .map(Member::getName)
                .findFirst()
                .orElse("");
        int likeCount = project.getLikes().size();

        // ToDo: 현재.. 예시로 하나 이상의 즐겨찾기가 있으면 true 사용자 식별이 가능해지면, 사용자가 북마크한 것으로 바꿔야함..
        boolean bookMark = !project.getFavorites().isEmpty();

        return ProjectResponse.builder()
                .thumbnailUrl(project.getThumbnail().getUuid()) // ToDo: MINIO로 바꾸기
                .projectName(project.getName())
                .teamName(project.getTeam())
                .studentsName(studentsName)
                .professorName(professorName)
                .projectType(project.getType())
                .projectCategory(project.getCategory())
                .techStack(List.of(project.getTechStack().split(", "))) // TechStack ', ' 기준으로 분리 <- ','와 ', ' 논의 ..?
                .likeCount(likeCount)
                .bookMark(bookMark)
                .build();
    }
}
