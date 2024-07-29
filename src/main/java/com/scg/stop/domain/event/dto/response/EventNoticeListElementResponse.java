package com.scg.stop.domain.event.dto.response;


import com.scg.stop.domain.event.domain.EventNotice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventNoticeListElementResponse {
    private Long id;
    private String title;
    private Integer hitCount;
    private boolean fixed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity -> DTO
    public static com.scg.stop.domain.notice.dto.response.NoticeListElementResponse from(EventNotice eventNotice) {
        return new com.scg.stop.domain.notice.dto.response.NoticeListElementResponse(
                eventNotice.getId(),
                eventNotice.getTitle(),
                eventNotice.getHitCount(),
                eventNotice.isFixed(),
                eventNotice.getCreatedAt(),
                eventNotice.getUpdatedAt()
        );
    }
}
