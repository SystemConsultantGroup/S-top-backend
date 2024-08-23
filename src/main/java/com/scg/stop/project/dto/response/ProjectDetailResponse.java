package com.scg.stop.project.dto.response;

import com.scg.stop.project.domain.*;
import com.scg.stop.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
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

    public static ProjectDetailResponse of(User user, Project project){
        List<String> studentNames = project.getMembers().stream()
                .filter(member -> member.getRole() == Role.STUDENT)
                .map(Member::getName)
                .collect(Collectors.toList());

        List<String> professorNames = project.getMembers().stream()
                .filter(member -> member.getRole() == Role.PROFESSOR)
                .map(Member::getName)
                .collect(Collectors.toList());

        List<String> techStackList = Arrays.stream(project.getTechStack().split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        Boolean like = project.getLikes().stream().anyMatch(likes -> likes.getUser().getId().equals(user.getId()));
        Boolean bookMark = project.getFavorites().stream().anyMatch(favoriteProject -> favoriteProject.getUser().getId().equals(user.getId()));

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
                techStackList,
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
