package com.scg.stop.project.dto.request;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.project.domain.*;
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

    @NotBlank(message = "기술 스택을 입력해주세요")
    private final String techStack; // ,로 구분

    @NotNull(message = "프로젝트 년도를 입력해주세요")
    private final Integer year;

    @NotNull(message = "수상 여부를 입력해주세요")
    private final AwardStatus awardStatus;

    @Valid
    @NotNull(message = "멤버를 입력해주세요")
    private final List<MemberRequest> members;

    public ProjectRequest(
            Long thumbnailId,
            Long posterId,
            String projectName,
            ProjectType projectType,
            ProjectCategory projectCategory,
            String teamName,
            String youtubeId,
            String techStack,
            Integer year,
            AwardStatus awardStatus,
            List<MemberRequest> members
    ) {

        validateTechStack(techStack); // 요청받은 기술 스택이 유효한지 검증

        this.thumbnailId = thumbnailId;
        this.posterId = posterId;
        this.projectName = projectName;
        this.projectType = projectType;
        this.projectCategory = projectCategory;
        this.teamName = teamName;
        this.youtubeId = youtubeId;
        this.techStack = techStack;
        this.year = year;
        this.awardStatus = awardStatus;
        this.members = members;
    }

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

    private void validateTechStack(String techStack) {
        // ', '으로 구분되어서 문자열이 구성이 되어있는지 확인하는 정규표현식
        String TECH_STACK_PATTERN = "^([\\w가-힣]+)(, [\\w가-힣]+)*$";

        if (!techStack.matches(TECH_STACK_PATTERN)) {
            throw new BadRequestException(ExceptionCode.INVALID_TECHSTACK);
        }
    }
}