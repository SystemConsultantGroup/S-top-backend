package com.scg.stop.domain.video.dto;

import com.scg.stop.domain.video.domain.Category;
import com.scg.stop.domain.video.domain.JobInterview;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class JobInterviewDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message = "제목을 입력해주세요.")
        private String title;

        @NotBlank(message = "유튜브 URL의 ID를 입력해주세요.")
        private String youtubeId;

        @NotNull(message = "연도를 입력해주세요.")
        private Integer year;

        @NotBlank(message = "카테고리를 입력해주세요.")
        private Category category;

        public JobInterview toEntity() {
            return JobInterview.builder()
                    .year(year)
                    .title(title)
                    .youtubeId(youtubeId)
                    .category(category)
                    .build();
        }
    }

    @Getter
    public static class Response {
        private Long id;
        private String title;
        private String youtubeId;
        private Integer year;
        private Category category;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        // Entity to DTO
        public Response(JobInterview jobInterview) {
            id = jobInterview.getId();
            title = jobInterview.getTitle();
            youtubeId = jobInterview.getYoutubeId();
            year = jobInterview.getYear();
            category = jobInterview.getCategory();
            createdAt = jobInterview.getCreatedAt();
            updatedAt = jobInterview.getUpdatedAt();
        }

    }
}
