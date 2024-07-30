package com.scg.stop.domain.event.dto.response;

import com.scg.stop.domain.event.domain.EventPeriod;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventPeriodResponse {

    private Long id;
    private Integer year;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EventPeriodResponse from(EventPeriod eventPeriod) {
        return new EventPeriodResponse(
                eventPeriod.getId(),
                eventPeriod.getYear(),
                eventPeriod.getStart(),
                eventPeriod.getEnd(),
                eventPeriod.getCreatedAt(),
                eventPeriod.getUpdatedAt()
        );
    }
}
