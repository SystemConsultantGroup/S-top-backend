package com.scg.stop.domain.video.dto.request;

import com.scg.stop.domain.video.domain.JobInterview;
import com.scg.stop.domain.video.domain.JobInterviewCategory;
import com.scg.stop.global.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class JobInterviewRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "유튜브 URL의 ID를 입력해주세요.")
    private String youtubeId;

    @NotNull(message = "연도를 입력해주세요.")
    private Integer year;

    @NotBlank(message = "대담자의 소속을 입력해주세요.")
    public String talkerBelonging;

    @NotBlank(message = "대담자의 성명을 입력해주세요.")
    public String talkerName;

    @ValidEnum(enumClass = JobInterviewCategory.class)
    @NotNull(message = "카테고리를 입력해주세요.")
    private JobInterviewCategory category;

    public JobInterview toEntity() {
        return JobInterview.from(
                title, youtubeId, year, talkerBelonging, talkerName, category
        );
    }
}
