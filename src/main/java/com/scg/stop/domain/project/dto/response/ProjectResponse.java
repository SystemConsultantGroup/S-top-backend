package com.scg.stop.domain.project.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.project.domain.Project;
import com.scg.stop.domain.project.domain.ProjectCategory;
import com.scg.stop.domain.project.domain.ProjectType;
import com.scg.stop.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<String> techStacks;
    private Integer likeCount;
    private Boolean like;
    private Boolean bookMark;

    public static ProjectResponse of(List<String> studentNames, List<String> professorNames, Boolean like, Boolean bookMark, Project project){
        List<String> techStackList = Arrays.stream(project.getTechStack().split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        return new ProjectResponse(
                project.getId(),
                FileResponse.from(project.getThumbnail()),
                project.getName(),
                project.getTeam(),
                studentNames,
                professorNames,
                project.getType(),
                project.getCategory(),
                techStackList,
                project.getLikes().size(),
                like,
                bookMark
        );
    }
}