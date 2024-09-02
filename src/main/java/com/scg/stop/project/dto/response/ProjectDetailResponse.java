package com.scg.stop.project.dto.response;

import com.scg.stop.project.domain.AwardStatus;
import com.scg.stop.project.domain.Project;
import com.scg.stop.project.domain.ProjectCategory;
import com.scg.stop.project.domain.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ProjectDetailResponse {

    private Long id;
    private FileResponse thumbnailInfo;
    private FileResponse posterInfo;
    private String projectName;
    private ProjectType projectType;
    private ProjectCategory projectCategory;
    private String teamName;
    private String youtubeId;
    private List<String> techStack;
    private Integer year;
    private AwardStatus awardStatus;
    private List<String> studentNames;
    private List<String> professorNames;
    private Integer likeCount;
    private Boolean like;
    private Boolean bookMark;
    private List<CommentResponse> comments;

    public static ProjectDetailResponse of(List<String> studentNames, List<String> professorNames, Boolean like, Boolean bookMark, Project project){
        List<CommentResponse> commentResponseList = project.getComments().stream()
                .map(CommentResponse::of)
                .collect(Collectors.toList());

        return new ProjectDetailResponse(
                project.getId(),
                FileResponse.from(project.getThumbnail()),
                FileResponse.from(project.getPoster()),
                project.getName(),
                project.getType(),
                project.getCategory(),
                project.getTeam(),
                project.getYoutubeId(),
                List.of(project.getTechStack().split(",")),
                project.getYear(),
                project.getAwardStatus(),
                studentNames,
                professorNames,
                project.getLikes().size(),
                like,
                bookMark,
                commentResponseList
        );
    }
}
