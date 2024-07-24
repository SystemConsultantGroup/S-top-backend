package com.scg.stop.domain.project.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.scg.stop.domain.project.domain.Member;
import com.scg.stop.domain.project.domain.Project;
import com.scg.stop.domain.project.domain.ProjectCategory;
import com.scg.stop.domain.project.domain.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
public class ProjectResponse {
    private String thumbnailUrl;
    private String projectName;
    private String teamName;
    private List<String> studentsName;
    private String professorName;
    private ProjectType projectType;
    private ProjectCategory projectCategory;
    private List<String> techStack;
    private int likeCount;
    private boolean bookMark;

    public static ProjectResponse of(List<String> studentsName, String professorName, Project project){
        return new ProjectResponse(
                project.getThumbnail().getUuid(),
                project.getName(),
                project.getTeam(),
                studentsName,
                professorName,
                project.getType(),
                project.getCategory(),
                List.of(project.getTechStack().split(", ")),
                project.getLikes().size(),
                !project.getFavorites().isEmpty()
        );
    }
}
