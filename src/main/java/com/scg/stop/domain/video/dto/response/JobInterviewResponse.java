package com.scg.stop.domain.video.dto.response;

import com.scg.stop.domain.video.domain.JobInterview;
import com.scg.stop.domain.video.domain.JobInterviewCategory;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class JobInterviewResponse {
    private Long id;
    private String title;
    private String youtubeId;
    private Integer year;
    private JobInterviewCategory category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity to DTO
    public JobInterviewResponse(JobInterview jobInterview) {
        id = jobInterview.getId();
        title = jobInterview.getTitle();
        youtubeId = jobInterview.getYoutubeId();
        year = jobInterview.getYear();
        category = jobInterview.getCategory();
        createdAt = jobInterview.getCreatedAt();
        updatedAt = jobInterview.getUpdatedAt();
    }
}
