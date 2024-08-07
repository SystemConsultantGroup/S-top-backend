package com.scg.stop.event.dto.response;


import com.scg.stop.event.domain.EventNotice;
import com.scg.stop.notice.dto.response.NoticeListElementResponse;
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
    public static NoticeListElementResponse from(EventNotice eventNotice) {
        return new NoticeListElementResponse(
                eventNotice.getId(),
                eventNotice.getTitle(),
                eventNotice.getHitCount(),
                eventNotice.isFixed(),
                eventNotice.getCreatedAt(),
                eventNotice.getUpdatedAt()
        );
    }
}
