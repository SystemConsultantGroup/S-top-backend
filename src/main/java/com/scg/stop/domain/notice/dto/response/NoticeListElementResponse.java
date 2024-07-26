package com.scg.stop.domain.notice.dto.response;

import com.scg.stop.domain.file.dto.response.FileResponse;
import com.scg.stop.domain.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListElementResponse {
    private Long id;
    private String title;
    private Integer hitCount;
    private boolean fixed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity -> DTO
    // TODO: handle attached files uuid
    public static NoticeListElementResponse from(Notice notice) {
        return new NoticeListElementResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getHitCount(),
                notice.isFixed(),
                notice.getCreatedAt(),
                notice.getUpdatedAt()
        );
    }
}