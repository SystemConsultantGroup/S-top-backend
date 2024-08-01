package com.scg.stop.domain.project.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.project.domain.Project;
import com.scg.stop.domain.project.domain.ProjectCategory;
import com.scg.stop.domain.project.domain.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private FileResponse thumbnailInfo;
    private String projectName;
    private String teamName;
    private List<String> studentNames;
    private List<String> professorNames;
    private ProjectType projectType;
    private ProjectCategory projectCategory;
    private List<String> techStack;
    private Integer likeCount;
    private Boolean bookMark;

    public static ProjectResponse of(List<String> studentNames, List<String> professorNames, Project project){
        return new ProjectResponse(
                project.getId(),
                FileResponse.from(project.getThumbnail()),
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
