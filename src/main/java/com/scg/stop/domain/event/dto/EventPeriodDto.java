package com.scg.stop.domain.event.dto;

import com.scg.stop.domain.event.domain.EventPeriod;
import com.scg.stop.domain.notice.domain.Notice;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

public class EventPeriodDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        @NotNull(message = "연도를 입력해주세요.")
        private Integer year;

        @NotNull(message = "이벤트 시작 일시를 입력해주세요.")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime start;

        @NotNull(message = "이벤트 종료 일시를 입력해주세요.")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime end;

        // DTO -> Entity
        public EventPeriod toEntity() {
            EventPeriod eventPeriod = EventPeriod.builder()
                    .year(year)
                    .start(start)
                    .end(end)
                    .build();
            return eventPeriod;
        }
    }

    @Getter
    public static class Response {

        private Long id;
        private Integer year;
        private LocalDateTime start;
        private LocalDateTime end;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        // Entity -> DTO
        public Response(EventPeriod eventPeriod) {
            id = eventPeriod.getId();
            year = eventPeriod.getYear();
            start = eventPeriod.getStart();
            end = eventPeriod.getEnd();
            createdAt = eventPeriod.getCreatedAt();
            updatedAt = eventPeriod.getUpdatedAt();
        }
    }
}
