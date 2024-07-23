package com.scg.stop.domain.event.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventPeriodResponse {

    private Long id;
    private Integer year;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
