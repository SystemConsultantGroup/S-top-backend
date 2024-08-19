package com.scg.stop.video.dto.response;

import com.scg.stop.video.domain.JobInterview;
import com.scg.stop.video.domain.JobInterviewCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JobInterviewUserResponse {

    private Long id;
    private String title;
    private String youtubeId;
    private Integer year;
    private String talkerBelonging;
    private String talkerName;
    private boolean isFavorite;
    private JobInterviewCategory category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity to DTO
    public static JobInterviewUserResponse from(JobInterview jobInterview) {
        return new JobInterviewUserResponse(
                jobInterview.getId(),
                jobInterview.getTitle(),
                jobInterview.getYoutubeId(),
                jobInterview.getYear(),
                jobInterview.getTalkerBelonging(),
                jobInterview.getTalkerName(),
                false,
                jobInterview.getCategory(),
                jobInterview.getCreatedAt(),
                jobInterview.getUpdatedAt()
        );
    }

    public static JobInterviewUserResponse from(JobInterview jobInterview, boolean isFavorite) {
        return new JobInterviewUserResponse(
                jobInterview.getId(),
                jobInterview.getTitle(),
                jobInterview.getYoutubeId(),
                jobInterview.getYear(),
                jobInterview.getTalkerBelonging(),
                jobInterview.getTalkerName(),
                isFavorite,
                jobInterview.getCategory(),
                jobInterview.getCreatedAt(),
                jobInterview.getUpdatedAt()
        );
    }

}
