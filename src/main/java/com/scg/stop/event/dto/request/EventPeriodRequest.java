package com.scg.stop.event.dto.request;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
public class EventPeriodRequest {

    @NotNull(message = "이벤트 시작 일시를 입력해주세요.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime start;

    @NotNull(message = "이벤트 종료 일시를 입력해주세요.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime end;
}
