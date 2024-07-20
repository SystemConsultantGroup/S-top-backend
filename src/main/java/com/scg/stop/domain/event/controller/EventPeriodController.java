package com.scg.stop.domain.event.controller;

import com.scg.stop.domain.event.domain.EventPeriod;
import com.scg.stop.domain.event.dto.EventPeriodDto;
import com.scg.stop.domain.event.dto.EventPeriodRequest;
import com.scg.stop.domain.event.dto.EventPeriodResponse;
import com.scg.stop.domain.event.service.EventPeriodService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/eventPeriods")
public class EventPeriodController {

    private final EventPeriodService eventPeriodService;

    @PostMapping
    public ResponseEntity<EventPeriodDto.Response> createEventPeriod(@RequestBody @Valid EventPeriodDto.Request requestDto) {
        EventPeriodDto.Response responseDto = eventPeriodService.createEventPeriod(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


}
