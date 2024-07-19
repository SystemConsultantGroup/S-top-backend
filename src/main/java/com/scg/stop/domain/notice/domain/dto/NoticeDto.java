package com.scg.stop.domain.notice.domain.dto;

import com.scg.stop.domain.notice.domain.Notice;
import lombok.*;

import java.time.LocalDateTime;

public class NoticeDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long id;
        private String title;
        private String content;
        private Integer hitCount;
        private boolean fixed;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;


        // DTO -> Entity
        public Notice toEntity() {
            Notice notice =  Notice.builder()
                    .id(id)
                    .title(title)
                    .content(content)
                    .hitCount(0)
                    .fixed(fixed)
                    .build();
            return notice;
        }
    }

    @Getter
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private Integer hitCount;
        private boolean fixed;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String[] fileUuids; // attached files uuid


        // Entity -> DTO
        // TODO: handle attached files uuid
        public Response(Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.hitCount = notice.getHitCount();
            this.fixed = notice.isFixed();
            this.createdAt = notice.getCreatedAt();
            this.updatedAt = notice.getUpdatedAt();
        }
    }
}
