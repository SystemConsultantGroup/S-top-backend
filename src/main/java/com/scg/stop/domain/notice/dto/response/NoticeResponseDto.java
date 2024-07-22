package com.scg.stop.domain.notice.dto.response;

import com.scg.stop.domain.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeResponseDto {
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
    public NoticeResponseDto(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.hitCount = notice.getHitCount();
        this.fixed = notice.isFixed();
        this.createdAt = notice.getCreatedAt();
        this.updatedAt = notice.getUpdatedAt();
    }
}