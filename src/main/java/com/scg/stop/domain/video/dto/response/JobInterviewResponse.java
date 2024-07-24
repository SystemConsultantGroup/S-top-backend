package com.scg.stop.domain.video.dto.response;

import com.scg.stop.domain.video.domain.JobInterview;
import com.scg.stop.domain.video.domain.JobInterviewCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class JobInterviewResponse {
    private Long id;
    private String title;
    private String youtubeId;
    private Integer year;
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
                jobInterview.getCategory(),
                jobInterview.getCreatedAt(),
                jobInterview.getUpdatedAt()
        );
    }
}
