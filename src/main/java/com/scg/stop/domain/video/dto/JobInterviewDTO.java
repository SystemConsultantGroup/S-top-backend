package com.scg.stop.domain.video.dto;

import com.scg.stop.domain.video.domain.Category;
import com.scg.stop.domain.video.domain.FavoriteVideo;
import com.scg.stop.domain.video.domain.JobInterview;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JobInterviewDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        @NotNull(message = "제목을 입력해주세요.")
        private String title;

        @NotNull(message = "유튜브 URL의 ID를 입력해주세요.")
        private String youtubeId;

        @NotNull(message = "연도를 입력해주세요.")
        private Integer year;

        @NotNull(message = "카테고리를 입력해주세요.")
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
