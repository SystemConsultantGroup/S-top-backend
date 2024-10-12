package com.scg.stop.project.dto.request;

import com.scg.stop.file.domain.File;
import com.scg.stop.project.domain.*;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.project.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProjectRequest {

    @NotNull(message = "썸네일 ID를 입력해주세요")
    private final Long thumbnailId;

    @NotNull(message = "포스터 ID를 입력해주세요")
    private final Long posterId;

    @NotBlank(message = "프로젝트 이름을 입력해주세요")
    private final String projectName;

    @NotNull(message = "프로젝트 타입을 입력해주세요")
    private final ProjectType projectType;

    @NotNull(message = "프로젝트 카테고리를 입력해주세요")
    private final ProjectCategory projectCategory;

    @NotBlank(message = "팀 이름을 입력해주세요")
    private final String teamName;

    @NotBlank(message = "프로젝트 yotubeId를 입력해주세요")
    private final String youtubeId;

    @NotNull(message = "프로젝트 년도를 입력해주세요")
    private final Integer year;

    @NotNull(message = "수상 여부를 입력해주세요")
    private final AwardStatus awardStatus;

    @Valid
    @NotNull(message = "멤버를 입력해주세요")
    private final List<MemberRequest> members;

    @NotBlank(message = "프로젝트 URL을 입력해주세요")
    private final String url;

    @NotBlank(message = "프로젝트 설명을 입력해주세요")
    private final String description;

    public ProjectRequest(
            Long thumbnailId,
            Long posterId,
            String projectName,
            ProjectType projectType,
            ProjectCategory projectCategory,
            String teamName,
            String youtubeId,
            Integer year,
            AwardStatus awardStatus,
            List<MemberRequest> members,
            String url,
            String description
    ) {

        this.thumbnailId = thumbnailId;
        this.posterId = posterId;
        this.projectName = projectName;
        this.projectType = projectType;
        this.projectCategory = projectCategory;
        this.teamName = teamName;
        this.youtubeId = youtubeId;
        this.year = year;
        this.awardStatus = awardStatus;
        this.members = members;
        this.url = url;
        this.description = description;
    }

    public Project toEntity(Long id, File thumbnail, File poster) {
        Project project =  new Project(
                id,
                projectName,
                projectType,
                projectCategory,
                teamName,
                youtubeId,
                year,
                url,
                description,
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