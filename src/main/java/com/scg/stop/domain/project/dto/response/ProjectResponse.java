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
    private String thumbnailUuid; // ToDo: File Entity 통째로 보내주는 것으로 논의?
    private String projectName;
    private String teamName;
    private List<String> studentNames;
    private List<String> professorNames;
    private ProjectType projectType;
    private ProjectCategory projectCategory;
    private List<String> techStack;
    private int likeCount;
    private boolean bookMark;

    public static ProjectResponse of(List<String> studentNames, List<String> professorNames, Project project){
        return new ProjectResponse(
                project.getThumbnail().getUuid(),
                project.getName(),
                project.getTeam(),
                studentNames,
                professorNames,
                project.getType(),
                project.getCategory(),
                List.of(project.getTechStack().split(", ")),
                project.getLikes().size(),
                !project.getFavorites().isEmpty()
        );
    }
}
