package com.scg.stop.domain.event.dto.response;

import com.scg.stop.domain.event.domain.EventNotice;
import com.scg.stop.domain.file.dto.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventNoticeResponse {
    private Long id;
    private String title;
    private String content;
    private Integer hitCount;
    private boolean fixed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FileResponse> files;

    // Entity -> DTO
    public static EventNoticeResponse from(EventNotice eventNotice, List<FileResponse> files) {
        return new EventNoticeResponse(
                eventNotice.getId(),
                eventNotice.getTitle(),
                eventNotice.getContent(),
                eventNotice.getHitCount(),
                eventNotice.isFixed(),
                eventNotice.getCreatedAt(),
                eventNotice.getUpdatedAt(),
                files
        );
    }
}
