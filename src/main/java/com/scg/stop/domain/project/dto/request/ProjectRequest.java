package com.scg.stop.domain.project.dto.request;

import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.project.domain.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ProjectRequest {

    @NotNull(message = "썸네일 ID를 입력해주세요")
    private Long thumbnailId;

    @NotNull(message = "포스터 ID를 입력해주세요")
    private Long posterId;

    @NotBlank(message = "프로젝트 이름을 입력해주세요")
    private String projectName;

    @NotNull(message = "프로젝트 타입을 입력해주세요")
    private ProjectType projectType;

    @NotNull(message = "프로젝트 카테고리를 입력해주세요")
    private ProjectCategory projectCategory;

    @NotBlank(message = "팀 이름을 입력해주세요")
    private String teamName;

    @NotBlank(message = "프로젝트 yotubeId를 입력해주세요")
    private String youtubeId;

    @NotBlank(message = "기술 스택을 입력해주세요")
    private String techStack; // ,로 구분

    @NotNull(message = "프로젝트 년도를 입력해주세요")
    private Integer year;

    @NotNull(message = "수상 여부를 입력해주세요")
    private AwardStatus awardStatus;

    @NotNull(message = "멤버를 입력해주세요")
    private List<MemberRequest> members;

    public Project toEntity(Long id, File thumbnail, File poster) {
        Project project =  new Project(
                id,
                projectName,
                projectType,
                projectCategory,
                teamName,
                youtubeId,
                techStack,
                year,
                awardStatus,
                thumbnail,
                poster,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        List<Member> memberEntities = members.stream()
                .map(memberRequest -> memberRequest.toEntity(project))
                .toList();

        project.getMembers().addAll(memberEntities);

        return project;
    }
}