package com.scg.stop.video.dto.response;

import com.scg.stop.video.domain.JobInterview;
import com.scg.stop.video.domain.JobInterviewCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JobInterviewResponse {
    private Long id;
    private String title;
    private String youtubeId;
    private Integer year;
    private String talkerBelonging;
    private String talkerName;
    private JobInterviewCategory category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity to DTO
    public static JobInterviewResponse from(JobInterview jobInterview) {
        return new JobInterviewResponse(
                jobInterview.getId(),
                jobInterview.getTitle(),
                jobInterview.getYoutubeId(),
                jobInterview.getYear(),
                jobInterview.getTalkerBelonging(),
                jobInterview.getTalkerName(),
                jobInterview.getCategory(),
                jobInterview.getCreatedAt(),
                jobInterview.getUpdatedAt()
        );
    }
}
