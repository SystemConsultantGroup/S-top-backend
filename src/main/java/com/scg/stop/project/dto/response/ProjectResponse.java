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
public class ProjectResponse {
    private Long id;
    private FileResponse thumbnailInfo;
    private String projectName;
    private String teamName;
    private List<String> studentNames;
    private List<String> professorNames;
    private ProjectType projectType;
    private ProjectCategory projectCategory;
    private AwardStatus awardStatus;
    private List<String> techStacks;
    private Integer year;
    private Integer likeCount;
    private Boolean like;
    private Boolean bookMark;

    public static ProjectResponse of(User user, Project project){
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

        Boolean like = user != null ? project.getLikes().stream().anyMatch(likes -> likes.getUser().getId().equals(user.getId())) : false;
        Boolean bookMark = user != null ? project.getFavorites().stream().anyMatch(favoriteProject -> favoriteProject.getUser().getId().equals(user.getId())) : false;

        return new ProjectResponse(
                project.getId(),
                FileResponse.from(project.getThumbnail()),
                project.getName(),
                project.getTeam(),
                studentNames,
                professorNames,
                project.getType(),
                project.getCategory(),
                project.getAwardStatus(),
                techStackList,
                project.getYear(),
                project.getLikes().size(),
                like,
                bookMark
        );
    }
}
