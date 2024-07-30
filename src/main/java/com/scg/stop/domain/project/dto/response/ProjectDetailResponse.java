package com.scg.stop.domain.project.dto.response;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.project.domain.AwardStatus;
import com.scg.stop.domain.project.domain.Project;
import com.scg.stop.domain.project.domain.ProjectCategory;
import com.scg.stop.domain.project.domain.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProjectDetailResponse {

    private Long id;
    private File thumbnailInfo;
    private File posterInfo;
    private String projectName;
    private ProjectType projectType;
    private ProjectCategory projectCategory;
    private String teamName;
    private String youtubeId;
    private List<String> techStack;
    private int year;
    private AwardStatus awardStatus;
    private List<String> sutudentNames;
    private List<String> professorNames;
    private int likeCount;
    private boolean bookMark;

    public static ProjectDetailResponse of(List<String> sutudentNames, List<String> professorNames, Project project){
        return new ProjectDetailResponse(
                project.getId(),
                project.getThumbnail(),
                project.getPoster(),
                project.getName(),
                project.getType(),
                project.getCategory(),
                project.getTeam(),
                project.getYoutubeId(),
                List.of(project.getTechStack().split(",")),
                project.getYear(),
                project.getAwardStatus(),
                sutudentNames,
                professorNames,
                project.getLikes().size(),
                !project.getFavorites().isEmpty()
        );
    }
}
