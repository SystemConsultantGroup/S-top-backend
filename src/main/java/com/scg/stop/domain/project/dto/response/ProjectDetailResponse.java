package com.scg.stop.domain.project.dto.response;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.project.domain.AwardStatus;
import com.scg.stop.domain.project.domain.Project;
import com.scg.stop.domain.project.domain.ProjectCategory;
import com.scg.stop.domain.project.domain.ProjectType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private Boolean bookMark;

    public static ProjectDetailResponse of(List<String> studentNames, List<String> professorNames, Project project){
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
                !project.getFavorites().isEmpty() // ToDo: 인증 설정 추가할 때, 인증된 사용자의 즐겨찾기 여부 확인 로직으로 변경
        );
    }
}
